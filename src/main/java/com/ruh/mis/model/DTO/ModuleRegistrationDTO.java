package com.ruh.mis.model.DTO;

import com.ruh.mis.model.GPAStatus;
import com.ruh.mis.model.ModuleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleRegistrationDTO {
    private int id;
    private int semesterId;
    private int intakeId;
    private int departmentId;
    private int studentId;
    private String studentName;
    private String studentRegNo;
    private int moduleId;
    private String moduleCode;
    private String moduleName;
    private ModuleType moduleType;
    private GPAStatus gpaStatus;
    private String status; // "Taken", "Not-Taken"
    private String registrationStatus; // "Pending", "Approved", "Rejected"
}