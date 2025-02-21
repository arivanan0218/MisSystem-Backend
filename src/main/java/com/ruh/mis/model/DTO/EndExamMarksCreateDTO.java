package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndExamMarksCreateDTO {
    private int studentId;
    private int endExamId;
    private double marksObtained;
}