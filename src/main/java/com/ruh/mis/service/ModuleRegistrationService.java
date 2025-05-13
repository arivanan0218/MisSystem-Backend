package com.ruh.mis.service;

import java.util.List;

import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;

public interface ModuleRegistrationService {
    void registerModules(ModuleRegistrationRequestDTO requestDTO);
    ModuleRegistrationResponseDTO getRegistrationDetailsForStudent(int studentId, int semesterId, int intakeId, int departmentId);
    List<ModuleRegistrationDTO> getRegistrationsByModuleId(int moduleId);
}