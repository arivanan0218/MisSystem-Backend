package com.ruh.mis.model.DTO;

import com.ruh.mis.model.GPAStatus;
import com.ruh.mis.model.ModuleType;

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
    private Integer credit; // Changed to Integer to allow null for GE modules
    private GPAStatus gpaStatus;
    private ModuleType moduleType; // Added module type
    private String moduleCoordinator;
}
