package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarksResponseDTO {
    private int studentId;
    private String studentName;
    private List<AssignmentMarksDTO> assignmentMarks;
    private double finalAssignmentMarks;
}
