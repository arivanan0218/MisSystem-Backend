package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "module_assignment_results")
public class ModuleAssignmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "module_result_id")
    private ModuleResult moduleResult;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    private double rawMarks;             // Original marks obtained
    private double maxPossibleMarks;     // Maximum possible marks (usually 100)
    private double assignmentPercentage; // The weight of this assignment in the module
    private double weightedMarks;        // Marks after applying the weight
    
    // Format for display: weightedMarks/maxWeightedMarks
    // For example: 15/20 for an assignment worth 20% where student got 75/100
    private String formattedMarks;
}
