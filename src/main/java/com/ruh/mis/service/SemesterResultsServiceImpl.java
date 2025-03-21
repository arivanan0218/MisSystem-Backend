package com.ruh.mis.service;

import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.ModuleResult;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.SemesterResults;
import com.ruh.mis.model.Student;
import com.ruh.mis.model.DTO.ModuleResultDTO;
import com.ruh.mis.model.DTO.SemesterResultsDTO;
import com.ruh.mis.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SemesterResultsServiceImpl implements SemesterResultsService {

    @Autowired
    private ModuleResultRepository moduleResultRepository;

    @Autowired
    private SemesterResultsRepository semesterResultsRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleResultService moduleResultService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public void calculateSemesterResults(int departmentId, int intakeId, int semesterId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        Intake intake = intakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found with ID: " + intakeId));
        
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found with ID: " + semesterId));

        // Get all modules for this semester
        List<com.ruh.mis.model.Module> semesterModules = moduleRepository.findBySemesterId(semesterId);
        
        if (semesterModules.isEmpty()) {
            throw new RuntimeException("No modules found for semester with ID: " + semesterId);
        }
        
        // Calculate module results for each module if not already calculated
        for (com.ruh.mis.model.Module module : semesterModules) {
            moduleResultService.calculateModuleResults(departmentId, intakeId, semesterId, module.getId());
        }

        // Get all students in this department, intake, and semester
        List<Student> students = studentRepository.findByDepartmentIdAndIntakeId(departmentId, intakeId);
        
        if (students.isEmpty()) {
            throw new RuntimeException("No students found for department ID: " + departmentId + " and intake ID: " + intakeId);
        }

        for (Student student : students) {
            // Get all module results for this student in this semester
            List<ModuleResult> moduleResults = new ArrayList<>();
            
            for (com.ruh.mis.model.Module module : semesterModules) {
                moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndStudentId(
                        departmentId, intakeId, semesterId, module.getId(), student.getId())
                        .ifPresent(moduleResults::add);
            }
            
            if (moduleResults.isEmpty()) {
                continue; // Skip if no module results found for this student
            }

            // Calculate semester GPA using the formula: (sum(credit * gradePoint)) / sum(credit)
            double totalCreditPoints = 0;
            double totalCredits = 0;
            
            for (ModuleResult result : moduleResults) {
                com.ruh.mis.model.Module module = result.getModule();
                // Only include GPA modules in calculation
                if ("GPA".equals(module.getGPA_Status())) {
                    double credit = module.getCredit();
                    double gradePoint = result.getGradePoint();
                    
                    totalCreditPoints += credit * gradePoint;
                    totalCredits += credit;
                }
            }
            
            double semesterGPA = (totalCredits > 0) ? (totalCreditPoints / totalCredits) : 0;
            String status = determineStatus(semesterGPA);
            
            // Save or update semester results
            Optional<SemesterResults> existingResultsOpt = semesterResultsRepository
                    .findByDepartmentIntakeSemesterAndStudent(departmentId, intakeId, semesterId, student.getId());
            
            SemesterResults semesterResults = existingResultsOpt.orElseGet(SemesterResults::new);
            semesterResults.setDepartment(department);
            semesterResults.setIntake(intake);
            semesterResults.setSemester(semester);
            semesterResults.setStudent(student);
            semesterResults.setSemesterGPA(semesterGPA);
            semesterResults.setStatus(status);
            
            semesterResultsRepository.save(semesterResults);
        }
    }

    private String determineStatus(double gpa) {
        if (gpa >= 3.7) return "First Class";
        else if (gpa >= 3.3) return "Second Upper";
        else if (gpa >= 3.0) return "Second Lower";
        else if (gpa >= 2.0) return "Pass";
        else return "Fail";
    }

    @Override
    public SemesterResultsDTO getSemesterResultsByStudent(int departmentId, int intakeId, int semesterId, int studentId) {
        // Get semester results for the student
        SemesterResults semesterResults = semesterResultsRepository
                .findByDepartmentIntakeSemesterAndStudent(departmentId, intakeId, semesterId, studentId)
                .orElseThrow(() -> new RuntimeException("Semester results not found for student with ID: " + studentId));
        
        // Map to DTO
        SemesterResultsDTO dto = modelMapper.map(semesterResults, SemesterResultsDTO.class);
        
        // Set additional fields
        dto.setDepartmentName(semesterResults.getDepartment().getDepartmentName());
        dto.setIntakeName(semesterResults.getIntake().getIntakeYear());
        dto.setSemesterName(semesterResults.getSemester().getSemesterName());
        dto.setStudentName(semesterResults.getStudent().getStudent_name());
        
        // Get all module results for this student in this semester
        List<com.ruh.mis.model.Module> semesterModules = moduleRepository.findBySemesterId(semesterId);
        List<ModuleResultDTO> moduleResultDTOs = new ArrayList<>();
        
        for (com.ruh.mis.model.Module module : semesterModules) {
            moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndStudentId(
                    departmentId, intakeId, semesterId, module.getId(), studentId)
                    .ifPresent(result -> {
                        ModuleResultDTO moduleResultDTO = modelMapper.map(result, ModuleResultDTO.class);
                        moduleResultDTO.setDepartmentName(result.getDepartment().getDepartmentName());
                        moduleResultDTO.setIntakeName(result.getIntake().getIntakeYear());
                        moduleResultDTO.setSemesterName(result.getSemester().getSemesterName());
                        moduleResultDTO.setModuleName(result.getModule().getModuleName());
                        moduleResultDTO.setStudentName(result.getStudent().getStudent_name());
                        moduleResultDTOs.add(moduleResultDTO);
                    });
        }
        
        dto.setModuleResults(moduleResultDTOs);
        
        return dto;
    }

    @Override
    public List<SemesterResultsDTO> getSemesterResults(int departmentId, int intakeId, int semesterId) {
        // Validate input parameters
        if (departmentId <= 0 || intakeId <= 0 || semesterId <= 0) {
            throw new IllegalArgumentException("Invalid input parameters: departmentId, intakeId, and semesterId must be positive");
        }
        
        // Get all semester results for the department, intake, and semester
        List<SemesterResults> semesterResultsList = semesterResultsRepository
                .findByDepartmentIntakeAndSemester(departmentId, intakeId, semesterId);
        
        if (semesterResultsList.isEmpty()) {
            // Return empty list instead of throwing exception
            return new ArrayList<>();
        }
        
        // Map to DTOs
        return semesterResultsList.stream().map(semesterResults -> {
            SemesterResultsDTO dto = modelMapper.map(semesterResults, SemesterResultsDTO.class);
            
            // Set additional fields with null checks
            if (semesterResults.getDepartment() != null) {
                dto.setDepartmentName(semesterResults.getDepartment().getDepartmentName());
            }
            
            if (semesterResults.getIntake() != null) {
                dto.setIntakeName(semesterResults.getIntake().getIntakeYear());
            }
            
            if (semesterResults.getSemester() != null) {
                dto.setSemesterName(semesterResults.getSemester().getSemesterName());
            }
            
            if (semesterResults.getStudent() != null) {
                dto.setStudentName(semesterResults.getStudent().getStudent_name());
            }
            
            // Get all module results for this student in this semester
            List<com.ruh.mis.model.Module> semesterModules = moduleRepository.findBySemesterId(semesterId);
            List<ModuleResultDTO> moduleResultDTOs = new ArrayList<>();
            
            if (semesterResults.getStudent() != null) {
                for (com.ruh.mis.model.Module module : semesterModules) {
                    moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndStudentId(
                            departmentId, intakeId, semesterId, module.getId(), semesterResults.getStudent().getId())
                            .ifPresent(result -> {
                                ModuleResultDTO moduleResultDTO = modelMapper.map(result, ModuleResultDTO.class);
                                
                                // Set additional fields with null checks
                                if (result.getDepartment() != null) {
                                    moduleResultDTO.setDepartmentName(result.getDepartment().getDepartmentName());
                                }
                                
                                if (result.getIntake() != null) {
                                    moduleResultDTO.setIntakeName(result.getIntake().getIntakeYear());
                                }
                                
                                if (result.getSemester() != null) {
                                    moduleResultDTO.setSemesterName(result.getSemester().getSemesterName());
                                }
                                
                                if (result.getModule() != null) {
                                    moduleResultDTO.setModuleName(result.getModule().getModuleName());
                                }
                                
                                if (result.getStudent() != null) {
                                    moduleResultDTO.setStudentName(result.getStudent().getStudent_name());
                                }
                                
                                moduleResultDTOs.add(moduleResultDTO);
                            });
                }
            }
            
            dto.setModuleResults(moduleResultDTOs);
            
            return dto;
        }).collect(Collectors.toList());
    }
}
