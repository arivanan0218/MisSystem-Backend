package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "intakes")
public class Intake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String intakeYear;
    private String batch;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "intake", cascade = CascadeType.ALL)
    private List<ModuleRegistration> registrations;

    @OneToMany(mappedBy = "intake", cascade = CascadeType.ALL)
    private List<ModuleResult> moduleResults;
}