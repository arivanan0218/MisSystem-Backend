package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentMarksCreateDTO {
    private int departmentId;
    private int intakeId;
    private int semesterId;
    private int moduleId;
    private int studentId;
    private double assignmentObtainedMarks;
}
