package com.ruh.mis.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String moduleName;
    private String moduleCode;
    private Integer credit; // Changed to Integer to allow null for GE modules
    
    @Enumerated(EnumType.STRING)
    private GPAStatus gpaStatus;
    
    @Enumerated(EnumType.STRING)
    private ModuleType moduleType; // Added module type
    
    private String moduleCoordinator;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "intake_id")
    private Intake intake;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "module_student",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<ModuleRegistration> registrations;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<ModuleResult> moduleResults;
}