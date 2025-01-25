package com.ruh.mis.model.DTO;

import com.ruh.mis.model.GPAStatus;
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
    private String studentName;
    private String studentReg;
    private String moduleCode;
    private GPAStatus gpaStatus;
}
