package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "results")
public class Results {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double score;
    private String grade;
    private String GPA;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
}
