package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;

import java.util.List;

public interface ModuleRegistrationService {
    void registerModules(ModuleRegistrationRequestDTO requestDTO);
    ModuleRegistrationResponseDTO getRegistrationDetailsForStudent(int studentId, int semesterId);
}
