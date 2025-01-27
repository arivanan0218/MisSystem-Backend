package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;

import java.util.List;

public interface ModuleRegistrationService {
    List<ModuleRegistrationDTO> findAll();

    List<ModuleRegistrationDTO> getModuleRegistrationByStudentId(int studentId);

    void saveModuleRegistrationList(List<ModuleRegistrationCreateDTO> moduleRegistrationCreateDTOList);
}
