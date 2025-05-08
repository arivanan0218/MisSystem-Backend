package com.ruh.mis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.DTO.ModuleResultDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.GPAStatus;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Marks;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.ModuleResult;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.AssignmentRepository;
import com.ruh.mis.repository.MarksRepository;
import com.ruh.mis.repository.ModuleResultRepository;
import com.ruh.mis.repository.ModuleAssignmentResultRepository;
import com.ruh.mis.model.ModuleAssignmentResult;

@Service
public class ModuleResultServiceImpl implements ModuleResultService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private ModuleResultRepository moduleResultRepository;
    
    @Autowired
    private ModuleAssignmentResultRepository moduleAssignmentResultRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public void calculateModuleResults(int departmentId, int intakeId, int semesterId, int moduleId) {
        List<Assignment> assignments = assignmentRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                departmentId, intakeId, semesterId, moduleId);

        if (assignments.isEmpty()) {
            System.out.println("No assignments found for the given module");
            return;
        }

        System.out.println("Found " + assignments.size() + " assignments for this module");
        
        Department department = assignments.get(0).getDepartment();
        Intake intake = assignments.get(0).getIntake();
        Semester semester = assignments.get(0).getSemester();
        Module module = assignments.get(0).getModule();

        // Get all students for this department and intake
        List<Student> students = marksRepository.findDistinctStudentsByAssignmentIn(
                assignments);
        
        System.out.println("Processing module results for " + students.size() + " students");

        // Process each student
        for (Student student : students) {
            double finalMarks = 0.0;
            
            System.out.println("Processing student: " + student.getStudentName() + " (" + student.getStudentRegNo() + ")");
            
            // Process each assignment to calculate final marks
            for (Assignment assignment : assignments) {
                // Find this student's mark for this assignment
                List<Marks> studentMarksForAssignment = marksRepository.findByStudentIdAndAssignmentId(
                        student.getId(), assignment.getId());
                
                if (!studentMarksForAssignment.isEmpty()) {
                    Marks mark = studentMarksForAssignment.get(0); // Get the first mark (should be only one)
                    double rawMarks = mark.getMarksObtained();
                    double weightedMark = rawMarks * (assignment.getAssignmentPercentage() / 100.0);
                    
                    System.out.println("  Assignment: " + assignment.getAssignmentName() + 
                                     " - Raw marks: " + rawMarks + 
                                     " - Weighted: " + weightedMark);
                    
                    finalMarks += weightedMark;
                } else {
                    System.out.println("  Assignment: " + assignment.getAssignmentName() + " - No marks found");
                }
            }

            // Calculate grade based on the module's GPA status and the marks obtained
            String grade = calculateGrade(finalMarks, module);
            double gradePoint = calculateGradePoint(grade);
            
            // A student passes the module if they score greater than or equal to 35% of the total marks (which is 100)
            String status = finalMarks >= 35.0 ? "PASS" : "FAIL";

            System.out.println("Student result: Final marks: " + finalMarks + 
                             ", Grade: " + grade + 
                             ", GPA: " + gradePoint + 
                             ", Status: " + status);

            // Check if a result already exists for this student and module
            Optional<ModuleResult> existingResultOpt = moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndStudentId(
                    departmentId, intakeId, semesterId, moduleId, student.getId());
            
            ModuleResult moduleResult;
            
            if (existingResultOpt.isPresent()) {
                ModuleResult existingResult = existingResultOpt.get();
                // Update the existing record
                existingResult.setFinalMarks(finalMarks);
                existingResult.setGrade(grade);
                existingResult.setGradePoint(gradePoint);
                existingResult.setStatus(finalMarks >= 35.0 ? "PASS" : "FAIL");
                moduleResult = moduleResultRepository.save(existingResult);
                
                // Delete existing assignment results to avoid duplicates
                moduleAssignmentResultRepository.deleteByModuleResultId(moduleResult.getId());
                
                System.out.println("Updated existing module result: " + moduleResult.getId() + 
                                 " - Final marks: " + finalMarks);
            } else {
                // Create a new record
                ModuleResult newResult = new ModuleResult();
                newResult.setDepartment(department);
                newResult.setIntake(intake);
                newResult.setSemester(semester);
                newResult.setModule(module);
                newResult.setStudent(student);
                newResult.setFinalMarks(finalMarks);
                newResult.setGrade(grade);
                newResult.setGradePoint(gradePoint);
                newResult.setStatus(finalMarks >= 35.0 ? "PASS" : "FAIL");
                moduleResult = moduleResultRepository.save(newResult);
                System.out.println("Created new module result: " + moduleResult.getId() + 
                                 " - Final marks: " + finalMarks);
            }
            
            // Now save the individual assignment results with weighted marks
            for (Assignment assignment : assignments) {
                // Find this student's mark for this assignment
                List<Marks> studentMarksForAssignment = marksRepository.findByStudentIdAndAssignmentId(
                        student.getId(), assignment.getId());
                
                if (!studentMarksForAssignment.isEmpty()) {
                    Marks mark = studentMarksForAssignment.get(0); // Get the first mark (should be only one)
                    double rawMarks = mark.getMarksObtained();
                    double assignmentPercent = assignment.getAssignmentPercentage();
                    double weightedMark = rawMarks * (assignmentPercent / 100.0);
                    
                    // Create and save the module assignment result
                    ModuleAssignmentResult assignmentResult = new ModuleAssignmentResult();
                    assignmentResult.setModuleResult(moduleResult);
                    assignmentResult.setAssignment(assignment);
                    assignmentResult.setRawMarks(rawMarks);
                    assignmentResult.setMaxPossibleMarks(100.0); // Assuming marks are out of 100
                    assignmentResult.setAssignmentPercentage(assignmentPercent);
                    assignmentResult.setWeightedMarks(weightedMark);
                    
                    // Format as requested: weightedMarks/maxWeightedMarks
                    // For example: 15/20 for an assignment worth 20% where student got 75/100
                    double maxWeightedMarks = assignmentPercent;
                    String formattedMarks = String.format("%.2f/%.2f", weightedMark, maxWeightedMarks);
                    assignmentResult.setFormattedMarks(formattedMarks);
                    
                    moduleAssignmentResultRepository.save(assignmentResult);
                    
                    System.out.println("  Saved assignment result: " + assignment.getAssignmentName() + 
                                    " - Raw marks: " + rawMarks + 
                                    " - Weighted: " + weightedMark + 
                                    " - Formatted: " + formattedMarks);
                }
            }
        }
        
        System.out.println("Module results calculation completed");
    }

    private String calculateGrade(double marks, Module module) {
        // For GPA Modules
        if (module.getGpaStatus() == GPAStatus.GPA) {
            if (marks >= 90) return "A+";
            else if (marks >= 80) return "A";
            else if (marks >= 75) return "A-";
            else if (marks >= 70) return "B+";
            else if (marks >= 65) return "B";
            else if (marks >= 60) return "B-";
            else if (marks >= 55) return "C+";
            else if (marks >= 50) return "C";
            else if (marks >= 45) return "C-";
            else return "E";
        } 
        // For Non-GPA Modules
        else {
            if (marks >= 70) return "H";       // High
            else if (marks >= 60) return "M";   // Medium
            else if (marks >= 45) return "S";   // Satisfactory
            else return "E";                   // Fail
        }
    }

    private double calculateGradePoint(String grade) {
        // GPA conversion based on grade as per the grading system
        return switch (grade) {
            case "A+" -> 4.0;
            case "A" -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "C-" -> 1.7;
            case "H" -> 0.0;  // Non-GPA grade, so no grade point
            case "M" -> 0.0;  // Non-GPA grade, so no grade point
            case "S" -> 0.0;  // Non-GPA grade, so no grade point
            default -> 0.0;    // E (Fail) or any other grade
        };
    }

    @Override
    public List<ModuleResultDTO> getModuleResults(int departmentId, int intakeId, int semesterId, int moduleId, int studentId) {
        List<ModuleResult> results = moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                departmentId, intakeId, semesterId, moduleId)
                .stream()
                .filter(result -> result.getStudent().getId() == studentId)
                .collect(Collectors.toList());

        return mapModuleResultsToDTOs(results);
    }
    
    @Override
    public List<ModuleResultDTO> getModuleResults(int departmentId, int intakeId, int semesterId, int moduleId) {
        List<ModuleResult> results = moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                departmentId, intakeId, semesterId, moduleId);

        return mapModuleResultsToDTOs(results);
    }
    
    @Override
    public List<ModuleResultDTO> getSemesterStudentResults(int departmentId, int intakeId, int semesterId, int studentId) {
        // Get all module results for a specific student in a specific semester
        List<ModuleResult> results = moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndStudentId(
                departmentId, intakeId, semesterId, studentId);
                
        return mapModuleResultsToDTOs(results);
    }
    
    // Helper method to map ModuleResult entities to ModuleResultDTOs with detailed assignment information
    private List<ModuleResultDTO> mapModuleResultsToDTOs(List<ModuleResult> results) {
        return results.stream().map(result -> {
            ModuleResultDTO dto = modelMapper.map(result, ModuleResultDTO.class);
            
            // Explicitly set IDs to ensure they're properly mapped
            dto.setId(result.getId());
            dto.setDepartmentId(result.getDepartment().getId());
            dto.setIntakeId(result.getIntake().getId());
            dto.setSemesterId(result.getSemester().getId());
            dto.setModuleId(result.getModule().getId());
            dto.setStudentId(result.getStudent().getId());
            
            // Also set the names and other details
            dto.setDepartmentName(result.getDepartment().getDepartmentName());
            dto.setIntakeName(result.getIntake().getIntakeYear());
            dto.setSemesterName(result.getSemester().getSemesterName());
            dto.setModuleName(result.getModule().getModuleName());
            dto.setStudentName(result.getStudent().getStudentName());
            dto.setStudentRegNo(result.getStudent().getStudentRegNo());
            
            // Explicitly set grade and status information
            dto.setFinalMarks(result.getFinalMarks());
            dto.setGrade(result.getGrade());
            dto.setGradePoint(result.getGradePoint());
            
            // If status is null, determine it based on assignments
            if (result.getStatus() == null) {
                dto.setStatus("PENDING"); // Default value
            } else {
                dto.setStatus(result.getStatus());
            }
            
            // Get assignments for this module
            List<Assignment> assignments = assignmentRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                    result.getDepartment().getId(), 
                    result.getIntake().getId(), 
                    result.getSemester().getId(), 
                    result.getModule().getId());
            
            // Get all marks for this student
            List<Marks> studentMarks = marksRepository.findByStudentId(result.getStudent().getId());
            
            // Map assignments to DTOs with detailed information
            List<ModuleResultDTO.AssignmentDetailDTO> assignmentDetails = new ArrayList<>();
            for (Assignment assignment : assignments) {
                ModuleResultDTO.AssignmentDetailDTO assignmentDto = new ModuleResultDTO.AssignmentDetailDTO();
                assignmentDto.setId(assignment.getId());
                assignmentDto.setAssignmentName(assignment.getAssignmentName());
                assignmentDto.setAssignmentPercentage(assignment.getAssignmentPercentage());
                
                // Find this student's mark for this assignment
                Optional<Marks> markOpt = studentMarks.stream()
                        .filter(m -> m.getAssignment().getId() == assignment.getId())
                        .findFirst();
                
                if (markOpt.isPresent()) {
                    Marks mark = markOpt.get();
                    double marksObtained = mark.getMarksObtained();
                    double weightedMarks = marksObtained * (assignment.getAssignmentPercentage() / 100.0);
                    
                    assignmentDto.setMarksObtained(marksObtained);
                    assignmentDto.setWeightedMarks(weightedMarks);
                } else {
                    // If no marks found, set defaults
                    assignmentDto.setMarksObtained(0);
                    assignmentDto.setWeightedMarks(0);
                }
                
                assignmentDetails.add(assignmentDto);
            }
            
            dto.setAssignmentDetails(assignmentDetails);
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * Updates all existing module results with the correct status based on final marks
     * This is an admin method to ensure all records have the proper status
     */
    @Transactional
    public void updateAllModuleResultStatuses() {
        List<ModuleResult> allResults = moduleResultRepository.findAll();
        int updatedCount = 0;
        
        System.out.println("Found " + allResults.size() + " module results to update");
        
        for (ModuleResult result : allResults) {
            double finalMarks = result.getFinalMarks();
            String currentStatus = result.getStatus();
            String newStatus = finalMarks >= 35.0 ? "PASS" : "FAIL";
            
            // Only update if status is null or different
            if (currentStatus == null || !currentStatus.equals(newStatus)) {
                result.setStatus(newStatus);
                moduleResultRepository.save(result);
                updatedCount++;
                
                System.out.println("Updated module result ID " + result.getId() + 
                                 " for student " + result.getStudent().getStudentName() + 
                                 " from " + (currentStatus == null ? "null" : currentStatus) + 
                                 " to " + newStatus + 
                                 " (final marks: " + finalMarks + ")");
            }
        }
        
        System.out.println("Updated " + updatedCount + " module results with correct status values");
    }
    
    @Override
    public List<ModuleResultDTO> getStudentModuleResults(int studentId, Integer departmentId, Integer intakeId, 
                                                     Integer semesterId, Integer moduleId) {
        List<ModuleResult> results;
        
        // Dynamically determine which repository method to use based on which parameters are provided
        if (departmentId != null && intakeId != null && semesterId != null && moduleId != null) {
            // If all parameters are provided, use the specific query
            results = moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                    departmentId, intakeId, semesterId, moduleId)
                    .stream()
                    .filter(result -> result.getStudent().getId() == studentId)
                    .collect(Collectors.toList());
        } else {
            // Otherwise, get all results for the student and filter in memory if needed
            results = moduleResultRepository.findByStudentId(studentId);
            
            // Apply optional filters if they are provided
            if (departmentId != null) {
                results = results.stream()
                        .filter(result -> result.getDepartment().getId() == departmentId)
                        .collect(Collectors.toList());
            }
            
            if (intakeId != null) {
                results = results.stream()
                        .filter(result -> result.getIntake().getId() == intakeId)
                        .collect(Collectors.toList());
            }
            
            if (semesterId != null) {
                results = results.stream()
                        .filter(result -> result.getSemester().getId() == semesterId)
                        .collect(Collectors.toList());
            }
            
            if (moduleId != null) {
                results = results.stream()
                        .filter(result -> result.getModule().getId() == moduleId)
                        .collect(Collectors.toList());
            }
        }
        
        // Map the results to DTOs
        return results.stream().map(result -> {
            ModuleResultDTO dto = modelMapper.map(result, ModuleResultDTO.class);
            
            // Explicitly set IDs to ensure they're properly mapped
            dto.setId(result.getId());
            dto.setDepartmentId(result.getDepartment().getId());
            dto.setIntakeId(result.getIntake().getId());
            dto.setSemesterId(result.getSemester().getId());
            dto.setModuleId(result.getModule().getId());
            dto.setStudentId(result.getStudent().getId());
            
            // Also set the names
            dto.setDepartmentName(result.getDepartment().getDepartmentName());
            dto.setIntakeName(result.getIntake().getIntakeYear());
            dto.setSemesterName(result.getSemester().getSemesterName());
            dto.setModuleName(result.getModule().getModuleName());
            dto.setStudentName(result.getStudent().getStudentName());
            
            return dto;
        }).collect(Collectors.toList());
    }
}