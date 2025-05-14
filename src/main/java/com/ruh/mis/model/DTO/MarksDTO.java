package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarksDTO {
    private int id;
    @Setter
    private int studentId;
    private int assignmentId;
    @Setter
    private String student_name;
    @Setter
    private String student_Reg_No; // Added field for student registration number
    private String assignmentName;
    private double marksObtained;
}