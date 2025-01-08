package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarksDTO {
    private int id;
    private int assignmentId;
    private int moduleId;
    private int semesterId;
    private int intakeId;
    private int departmentId;
    private int studentId; // Change this to a list of student IDs to represent many students
    private String studentName; // Optionally, if you need student names
    private double obtainedMarks;
}
