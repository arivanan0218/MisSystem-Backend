package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    // Custom setters needed for handling ID mapping issues in some environments
    private int id;
    private int departmentId;
    //    private int moduleId;
//    private int semesterId;
    private int intakeId;
    private String firstName;
    private String lastName;
    private String studentName; // Computed field (firstName + lastName)
    private String studentRegNo;
    private String studentNIC;
    private String studentMail;
    private String phoneNumber;
    private String username;
    private String password;
    private String gender;
    private LocalDate dateOfBirth;

}