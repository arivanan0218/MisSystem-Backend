package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleRegistrationCreateDTO {
    private int departmentId;
    private int intakeId;
    private int semesterId;
    private String student_name;
    private String student_Reg_No;
    private String moduleName;
    private String moduleCode;
    private int credit;
    private String GPA_Status;
}
