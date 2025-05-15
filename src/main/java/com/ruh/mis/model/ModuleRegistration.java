package com.ruh.mis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "moduleRegistration")
public class ModuleRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "intake_id", nullable = false)
    private Intake intake;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // "Taken" or "Not-Taken"
    private String status;
    
    // "G" for GPA, "N" for NGPA, "-" for Not Taken
    private String grade;
    
    // "Pending", "Approved", "Rejected"
    private String registrationStatus;
    
    @PrePersist
    public void prePersist() {
        if (this.registrationStatus == null) {
            this.registrationStatus = "Pending";
        }
    }
}
