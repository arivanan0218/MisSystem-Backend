package com.ruh.mis.model.DTO;

import com.ruh.mis.model.GPAStatus;
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
    private int studentId;
    private int moduleId;
    private GPAStatus gpaStatus;
}
