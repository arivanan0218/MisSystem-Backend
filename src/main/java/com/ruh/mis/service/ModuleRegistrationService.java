package com.ruh.mis.service;

import java.util.List;

import com.ruh.mis.model.DTO.AdminRegistrationUpdateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;

public interface ModuleRegistrationService {
    // Student operations
    void registerModules(ModuleRegistrationRequestDTO requestDTO);
    ModuleRegistrationResponseDTO getRegistrationDetailsForStudent(int studentId, int semesterId, int intakeId, int departmentId);
    
    // Admin operations
    List<ModuleRegistrationDTO> getRegistrationsByModuleId(int moduleId);
    List<ModuleRegistrationDTO> getPendingRegistrationsByDepartmentIntakeSemester(int departmentId, int intakeId, int semesterId);
    List<ModuleRegistrationDTO> getPendingRegistrationsByModule(int moduleId, int semesterId, int intakeId, int departmentId);
    ModuleRegistrationDTO updateRegistrationStatus(AdminRegistrationUpdateDTO updateDTO);
}