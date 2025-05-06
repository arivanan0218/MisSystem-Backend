package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transcripts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transcript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "intake_id")
    private Intake intake;

    private double overallGPA;
    private int totalCredits;

    // We won't store semester results directly to avoid circular references
    // Instead, we'll fetch them when needed through the service layer

    // We'll use a transient field to store module results grouped by semester
    @Transient
    private List<ModuleResult> allModuleResults = new ArrayList<>();
}
