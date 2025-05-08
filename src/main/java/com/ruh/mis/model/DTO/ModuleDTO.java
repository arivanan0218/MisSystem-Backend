package com.ruh.mis.model.DTO;

import com.ruh.mis.model.GPAStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {
    private int id;
    private int departmentId;
    private int intakeId;
    private int semesterId;
    private String moduleName;
    private String moduleCode;
    private int credit;
    private GPAStatus gpaStatus; // Changed from String GPA_Status for consistency
    private String moduleCoordinator;
}
