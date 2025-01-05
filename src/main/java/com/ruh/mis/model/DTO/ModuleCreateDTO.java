package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCreateDTO {
    private int departmentId;
    private int intakeId;
    private int semesterId;
    private String moduleName;
    private String moduleCode;
    private int credit;
    private String GPA_Status;
    private String moduleCoordinator;
}
