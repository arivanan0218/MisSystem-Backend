package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleRegistrationRequestDTO {
    private int studentId;
    private int semesterId;
    private int intakeId;
    private int departmentId;
    private List<TakenModuleDTO> takenModules; // List of Module IDs with GPA status
}
