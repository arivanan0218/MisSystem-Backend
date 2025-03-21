package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String student_name;
    private String student_Reg_No;
    private String student_email;
    private String student_NIC;

    @ManyToOne
    @JoinColumn(name = "intake_id")
    private Intake intake;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<ModuleRegistration> registrations;

    @ManyToMany(mappedBy = "students")
    private List<Module> modules;

    @ManyToMany(mappedBy = "students")
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<ModuleResult> moduleResults;
}
