package com.ruh.mis.model.DTO;

import lombok.Data;
import java.util.List;

@Data
public class SemesterResultsDTO {
    private int id;
    private int departmentId;
    private String departmentName;
    private int intakeId;
    private String intakeName;
    private int semesterId;
    private String semesterName;
    private int studentId;
    private String studentName;
    private double semesterGPA;
    private String status;
    private List<ModuleResultDTO> moduleResults;
}
