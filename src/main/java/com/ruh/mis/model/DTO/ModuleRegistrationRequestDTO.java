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
    private List<Integer> takenModuleIds; // List of Module IDs
}
