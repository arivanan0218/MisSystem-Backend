package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "final_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "intake_id")
    private Intake intake;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "overall_gpa")
    private double overallGPA;

    @Column(name = "status")
    private String status;

    @ElementCollection
    @CollectionTable(name = "final_results_semester_gpas", 
                    joinColumns = @JoinColumn(name = "final_results_id"))
    @Column(name = "semester_gpa")
    private List<Double> semesterGPAs = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "final_results_semester_names", 
                    joinColumns = @JoinColumn(name = "final_results_id"))
    @Column(name = "semester_name")
    private List<String> semesterNames = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "final_results_semester_weights", 
                    joinColumns = @JoinColumn(name = "final_results_id"))
    @Column(name = "semester_weight")
    private List<Double> semesterWeights = new ArrayList<>();
}
