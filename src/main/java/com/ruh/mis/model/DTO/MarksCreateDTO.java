package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarksCreateDTO {
    private int assignmentId;
    private int moduleId;
    private int semesterId;
    private int intakeId;
    private int departmentId;
    private int studentId;
    private double obtainedMarks;
}
