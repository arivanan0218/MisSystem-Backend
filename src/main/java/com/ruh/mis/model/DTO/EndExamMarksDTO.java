package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndExamMarksDTO {
    private int id;
    private int studentId;
    private int moduleId;
    private int endExamId;
    private double marksObtained;
    private String student_name;
    private String endExamName;

    private int endExamPercentage;
}