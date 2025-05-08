package com.ruh.mis.model.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptDTO {
    // Student information
    private int studentId;
    private String studentName;
    private String studentRegNo;
    
    // Department and intake information
    private int departmentId;
    private String departmentName;
    private int intakeId;
    private String intakeName;
    
    // Overall information
    private double overallGpa;
    private int totalCredits;
    
    // Semester-wise information
    private List<SemesterTranscriptDTO> semesters = new ArrayList<>();
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SemesterTranscriptDTO {
        private int semesterId;
        private String semesterName;
        private double semesterGPA;
        private List<ModuleTranscriptDTO> modules = new ArrayList<>();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModuleTranscriptDTO {
        private int moduleId;
        private String moduleCode;
        private String moduleName;
        private int moduleCredits;
        private String moduleGrade;
        private double moduleGradePoint;
    }
}
