package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndExamMarksResponseDTO {
    private int studentId;
    private String student_name;
    private List<EndExamMarksDTO> endExamMarks;
    private double finalEndExamMarks;
}
