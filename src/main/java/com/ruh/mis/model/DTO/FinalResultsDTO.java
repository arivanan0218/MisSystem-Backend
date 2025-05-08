package com.ruh.mis.model.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalResultsDTO {
    private int id;
    private int departmentId;
    private String departmentName;
    private int intakeId;
    private String intakeName;
    private int studentId;
    private String studentName;
    private double overallGpa;
    private String status;
    private List<Double> semesterGpas = new ArrayList<>();
    private List<String> semesterNames = new ArrayList<>();
    private List<Double> semesterWeights = new ArrayList<>();
    
    // Student details
    private String studentRegNo;
}
