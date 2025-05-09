package com.ruh.mis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruh.mis.model.DTO.FinalResultsDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.FinalResults;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.SemesterResults;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.FinalResultsRepository;
import com.ruh.mis.repository.IntakeRepository;
import com.ruh.mis.repository.SemesterResultsRepository;
import com.ruh.mis.repository.StudentRepository;

@Service
public class FinalResultsServiceImpl implements FinalResultsService {

    private static final Logger logger = LoggerFactory.getLogger(FinalResultsServiceImpl.class);
    
    @Autowired
    private FinalResultsRepository finalResultsRepository;
    
    @Autowired
    private SemesterResultsRepository semesterResultsRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private IntakeRepository intakeRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * Get the weight for a specific semester ID
     * 
     * @param semesterId The semester ID
     * @return The weight for the semester
     */
    private double getSemesterWeight(int semesterId) {
        // Since we don't have a direct semesterNumber field, we'll use a simple mapping
        // based on semester ID order (assuming IDs 1-2 are for semesters 1-2, etc.)
        // Weights as defined in the requirements
        // 0.05 for Semester 1-2
        // 0.15 for Semester 3-8
        if (semesterId <= 2) {
            return 0.05;
        } else if (semesterId <= 8) {
            return 0.15;
        } else {
            // Default weight for any additional semesters
            return 0.0;
        }
    }
    
    /**
     * Calculate the OGPA based on semester GPAs and their weights using the formula:
     * OGPA = Σᵏ[( Σⁿⱼ₌₁ cⱼ GPVⱼ / Σⁿⱼ₌₁ cⱼ ) × (Wᵢ)]
     * 
     * Where:
     * - n is the number of modules in semester i
     * - GPVⱼ is the Grade Point Value for module j
     * - cⱼ is the number of credits for module j
     * - Wᵢ is the weight for semester i (0.05 for Semesters 1-2, 0.15 for Semesters 3-8)
     * 
     * @param semesterGpas List of semester GPAs (already calculated as Σⁿⱼ₌₁ cⱼ GPVⱼ / Σⁿⱼ₌₁ cⱼ for each semester)
     * @param semesterWeights List of semester weights - these will be ignored in favor of using standard weights
     * @return Calculated overall GPA
     */
    private double calculateOGPA(List<Double> semesterGpas, List<Double> semesterWeights) {
        double ogpa = 0.0;
        double totalWeight = 0.0;
        
        // Derive semester numbers from the index - assuming they're in order from 1 to n
        for (int i = 0; i < semesterGpas.size(); i++) {
            // Convert zero-based index to one-based semester number
            int semesterNum = i + 1;
            
            // Determine weight based on semester number as per specification in the academic rules
            // 0.05 for Semesters 1-2, 0.15 for Semesters 3-8
            double weight = (semesterNum <= 2) ? 0.05 : 0.15;
            
            // Add weighted semester GPA to OGPA
            ogpa += semesterGpas.get(i) * weight;
            totalWeight += weight;
        }
        
        // Calculate the final weighted average
        return (totalWeight > 0) ? (ogpa / totalWeight) : 0.0;
    }
    
    /**
     * Determine the final status based on OGPA
     * 
     * @param ogpa The Overall GPA
     * @return The status string
     */
    private String determineStatus(double ogpa) {
        if (ogpa >= 3.70) {
            return "First Class";
        } else if (ogpa >= 3.30) {
            return "Second Class Upper Division";
        } else if (ogpa >= 3.00) {
            return "Second Class Lower Division";
        } else if (ogpa >= 2.00) {
            return "Pass";
        } else {
            return "Fail";
        }
    }

    @Override
    @Transactional
    public boolean calculateFinalResults(int departmentId, int intakeId) {
        logger.info("Calculating final results for department: {}, intake: {}", departmentId, intakeId);
        
        try {
            // Validate department and intake
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
            
            Intake intake = intakeRepository.findById(intakeId)
                    .orElseThrow(() -> new IllegalArgumentException("Intake not found"));
            
            // Get all students in the department and intake
            List<Student> students = studentRepository.findByDepartmentAndIntake(departmentId, intakeId);
            
            if (students.isEmpty()) {
                logger.warn("No students found for department: {}, intake: {}", departmentId, intakeId);
                return false;
            }
            
            // Process each student
            for (Student student : students) {
                // Get all semester results for the student
                List<SemesterResults> allSemesterResults = semesterResultsRepository.findByStudent(student.getId());
                
                if (allSemesterResults.isEmpty()) {
                    logger.warn("No semester results found for student: {}", student.getId());
                    continue;
                }
                
                // Create or update final results for the student
                FinalResults finalResults = finalResultsRepository
                        .findByDepartmentIntakeAndStudent(departmentId, intakeId, student.getId())
                        .orElse(new FinalResults());
                
                finalResults.setDepartment(department);
                finalResults.setIntake(intake);
                finalResults.setStudent(student);
                
                // Clear existing semester data
                finalResults.getSemesterGpas().clear();
                finalResults.getSemesterNames().clear();
                finalResults.getSemesterWeights().clear();
                
                // Map to store semester number and its GPA
                Map<Integer, Double> semesterGpaMap = new HashMap<>();
                Map<Integer, String> semesterNameMap = new HashMap<>();
                
                // Collect all semester GPAs
                for (SemesterResults semesterResult : allSemesterResults) {
                    // Use semester ID as the key since there's no semesterNumber field
                    int semesterId = semesterResult.getSemester().getId();
                    double semesterGpa = semesterResult.getSemesterGPA();
                    String semesterName = semesterResult.getSemester().getSemesterName();
                    
                    semesterGpaMap.put(semesterId, semesterGpa);
                    semesterNameMap.put(semesterId, semesterName);
                }
                
                // Sort semesters by number and add to lists
                List<Integer> sortedSemesters = new ArrayList<>(semesterGpaMap.keySet());
                sortedSemesters.sort(Integer::compareTo);
                
                List<Double> semesterGpas = new ArrayList<>();
                List<String> semesterNames = new ArrayList<>();
                List<Double> semesterWeights = new ArrayList<>();
                
                for (Integer semesterNumber : sortedSemesters) {
                    semesterGpas.add(semesterGpaMap.get(semesterNumber));
                    semesterNames.add(semesterNameMap.get(semesterNumber));
                    semesterWeights.add(getSemesterWeight(semesterNumber));
                }
                
                // Set the lists to the final results
                finalResults.setSemesterGpas(semesterGpas);
                finalResults.setSemesterNames(semesterNames);
                finalResults.setSemesterWeights(semesterWeights);
                
                // Calculate OGPA
                double ogpa = calculateOGPA(semesterGpas, semesterWeights);
                finalResults.setOverallGpa(ogpa);
                
                // Determine status
                finalResults.setStatus(determineStatus(ogpa));
                
                // Save final results
                finalResultsRepository.save(finalResults);
                
                logger.info("Final results calculated for student: {}, OGPA: {}", student.getId(), ogpa);
            }
            
            return true;
        } catch (Exception e) {
            logger.error("Error calculating final results: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<FinalResultsDTO> getFinalResults(int departmentId, int intakeId) {
        logger.info("Getting final results for department: {}, intake: {}", departmentId, intakeId);
        
        List<FinalResults> finalResults = finalResultsRepository.findByDepartmentAndIntake(departmentId, intakeId);
        
        return finalResults.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FinalResultsDTO getFinalResultsByStudent(int departmentId, int intakeId, int studentId) {
        logger.info("Getting final results for student: {} in department: {}, intake: {}", 
                studentId, departmentId, intakeId);
        
        Optional<FinalResults> finalResults = finalResultsRepository
                .findByDepartmentIntakeAndStudent(departmentId, intakeId, studentId);
        
        return finalResults.map(this::convertToDTO).orElse(null);
    }
    
    /**
     * Convert FinalResults entity to FinalResultsDTO
     * 
     * @param finalResults The FinalResults entity
     * @return FinalResultsDTO
     */
    private FinalResultsDTO convertToDTO(FinalResults finalResults) {
        FinalResultsDTO dto = modelMapper.map(finalResults, FinalResultsDTO.class);
        
        // Set additional fields that are not directly mapped
        dto.setDepartmentId(finalResults.getDepartment().getId());
        dto.setDepartmentName(finalResults.getDepartment().getDepartmentName());
        dto.setIntakeId(finalResults.getIntake().getId());
        dto.setIntakeName(finalResults.getIntake().getIntakeYear() + " - " + finalResults.getIntake().getBatch());
        
        // Set student details
        Student student = finalResults.getStudent();
        dto.setStudentId(student.getId());
        dto.setStudentName(student.getStudentName());
        dto.setStudentRegNo(student.getStudentRegNo());
        
        return dto;
    }
}
