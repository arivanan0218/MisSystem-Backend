package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarksCreateDTO {
    private int assignmentId;
    private int moduleId;
    private int semesterId;
    private int intakeId;
    private int departmentId;
    private String registerNo;
    private String studentName;
    private double obtainedMarks;
}
