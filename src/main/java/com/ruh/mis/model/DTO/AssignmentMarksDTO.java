package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentMarksDTO {
    private int assignmentId;
    private String assignmentName;
    private double marksObtained;
    private int assignmentPercentage;
    private int moduleId;
}
