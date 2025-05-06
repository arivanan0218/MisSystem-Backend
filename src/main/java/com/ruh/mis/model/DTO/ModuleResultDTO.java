package com.ruh.mis.model.DTO;

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
    private double finalMarks;
    private String grade;
    private double gradePoint;
}