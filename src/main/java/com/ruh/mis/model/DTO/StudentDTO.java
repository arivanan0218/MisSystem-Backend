package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private int id;
    private int departmentId;
    //    private int moduleId;
//    private int semesterId;
    private int intakeId;
    private String name;
    private String regNo;
    private String nic;
    private String email;
    private String phoneNumber;
    private String username;
    private String password;

}