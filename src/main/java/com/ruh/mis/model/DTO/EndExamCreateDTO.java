package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndExamCreateDTO {
    private int moduleId;
    private int semesterId;
    private int intakeId;
    private int departmentId;
    private String endExamName;
    private int endExamPercentage;
}