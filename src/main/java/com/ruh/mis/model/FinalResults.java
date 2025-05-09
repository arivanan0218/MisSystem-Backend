package com.ruh.mis.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double overallGpa;

    @Column(name = "status")
    private String status;

    @ElementCollection
    @CollectionTable(name = "final_results_semester_gpas", 
                    joinColumns = @JoinColumn(name = "final_results_id"))
    @Column(name = "semester_gpa")
    private List<Double> semesterGpas = new ArrayList<>();

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
