package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateDTO {
    private int departmentId;
    //    private int moduleId;
//    private int semesterId;
    private int intakeId;
    private String studentRegNo;
    private String firstName;
    private String lastName;
    // studentName will be automatically generated from firstName + lastName
    private String studentNIC;
    private String studentMail;
    private String phoneNumber;
    private String username;
    private String password;
    private String gender;
    private LocalDate dateOfBirth;

}
