package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private double overallGPA;
    private String status;
    private List<Double> semesterGPAs = new ArrayList<>();
    private List<String> semesterNames = new ArrayList<>();
    private List<Double> semesterWeights = new ArrayList<>();
}
