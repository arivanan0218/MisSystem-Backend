package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentMarksDTO {
    private int id;
    private int departmentId;
    private int intakeId;
    private int semesterId;
    private int moduleId;
    private String studentRegNo;
    private String studentName;
    private double assignmentObtainedMarks;
}
