package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarksDTO {
    private int id;
    @Setter
    private int studentId;
    private int assignmentId;
    @Setter
    private String studentName;
    private String assignmentName;
    private double marksObtained;
}
