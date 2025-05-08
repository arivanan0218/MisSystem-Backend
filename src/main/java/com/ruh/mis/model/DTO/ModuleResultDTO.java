package com.ruh.mis.model.DTO;

import java.util.List;

import lombok.Data;

@Data
public class ModuleResultDTO {
    private int id;
    private int departmentId;
    private String departmentName;
    private int intakeId;
    private String intakeName;
    private int semesterId;
    private String semesterName;
    private int moduleId;
    private String moduleName;
    private int studentId;
    private String studentName;
    private String studentRegNo;
    
    // Assignment details
    private List<AssignmentDetailDTO> assignmentDetails;
    
    private double finalMarks;
    private String grade;
    private double gradePoint;
    private String status; // PASS or FAIL
    
    @Data
    public static class AssignmentDetailDTO {
        private int id;
        private String assignmentName;
        private double assignmentPercentage;
        private double marksObtained; // Raw marks
        private double weightedMarks; // Marks after applying percentage
    }
}