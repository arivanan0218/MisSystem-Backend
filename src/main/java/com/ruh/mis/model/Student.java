package com.ruh.mis.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    
    // No backing field for studentName - it will be a computed property
    private String studentRegNo;
    private String studentNIC;
    private String studentMail;
    private String phoneNumber;
    private String username;
    private String password;
    
    private String gender; // Male, Female, Other
    private LocalDate dateOfBirth; // Using LocalDate for date handling


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
    
    // Getter for the computed studentName field
    public String getStudentName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        } else {
            return ""; // Return empty string if both are null
        }
    }
    
    // Setter for studentName that splits the full name into firstName and lastName
    public void setStudentName(String fullName) {
        if (fullName != null && !fullName.trim().isEmpty()) {
            String[] nameParts = fullName.trim().split("\\s+", 2);
            this.firstName = nameParts[0];
            this.lastName = nameParts.length > 1 ? nameParts[1] : "";
        }
    }
}
