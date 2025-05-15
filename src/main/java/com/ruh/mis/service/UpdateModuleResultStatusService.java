package com.ruh.mis.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruh.mis.model.ModuleResult;
import com.ruh.mis.repository.ModuleResultRepository;

@Service
public class UpdateModuleResultStatusService {
    
    @Autowired
    private ModuleResultRepository moduleResultRepository;
    
    @Transactional
    public boolean updateModuleResultStatus(int resultId, String newStatus) {
        try {
            // Validate status
            if (!"PASS".equals(newStatus) && !"FAIL".equals(newStatus)) {
                System.out.println("Invalid status: " + newStatus + ". Status must be PASS or FAIL.");
                return false;
            }
            
            Optional<ModuleResult> resultOpt = moduleResultRepository.findById(resultId);
            
            if (resultOpt.isPresent()) {
                ModuleResult result = resultOpt.get();
                
                // Update the status
                result.setStatus(newStatus);
                
                // If we're setting to PASS but the marks are below 35, adjust the marks to 35
                if ("PASS".equals(newStatus) && result.getFinalMarks() < 35.0) {
                    result.setFinalMarks(35.0);
                    
                    // Update grade and grade point based on new marks
                    String newGrade = calculateGrade(35.0, result.getModule());
                    double newGradePoint = calculateGradePoint(newGrade);
                    
                    result.setGrade(newGrade);
                    result.setGradePoint(newGradePoint);
                    
                    System.out.println("Student " + result.getStudent().getStudentName() + 
                                  " marks adjusted to 35 due to manual PASS status assignment. " +
                                  "New grade: " + newGrade);
                } else if ("FAIL".equals(newStatus) && !result.getGrade().equals("E")) {
                    // If setting to FAIL and grade is not already E, we might need to update the grade
                    // Note: This is a business rule that you may want to adjust based on your requirements
                    
                    if (result.getFinalMarks() >= 35.0) {
                        // We don't change the marks, but we do update the grade to "E" and GPA to 0.0
                        result.setGrade("E");
                        result.setGradePoint(0.0);
                        
                        System.out.println("Student " + result.getStudent().getStudentName() + 
                                       " grade changed to E due to manual FAIL status assignment. " +
                                       "Marks unchanged at " + result.getFinalMarks());
                    }
                }
                
                moduleResultRepository.save(result);
                
                System.out.println("Updated status for student " + result.getStudent().getStudentName() + 
                              " (" + result.getStudent().getStudentRegNo() + ") " +
                              "to " + newStatus);
                
                return true;
            } else {
                System.out.println("Module result with ID " + resultId + " not found");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error updating module result status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // These methods should match those in ModuleResultServiceImpl
    private String calculateGrade(double marks, com.ruh.mis.model.Module module) {
        // For GPA Modules
        if (module.getGpaStatus() == com.ruh.mis.model.GPAStatus.GPA) {
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
}