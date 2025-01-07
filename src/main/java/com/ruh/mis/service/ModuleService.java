package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.Module;

import java.util.List;

public interface ModuleService {
    List<ModuleDTO> findAll();

    List<ModuleRegistrationDTO> findAllRegistration();

    ModuleDTO findById(int theId);

    ModuleRegistrationDTO findByRegistrationId(int theRegistrationId);

    List<ModuleDTO> getModuleByDepartmentIdAndIntakeIdAndSemesterId(int departmentId, int intakeId, int semesterId);

    List<ModuleRegistrationDTO> getModuleRegistrationByDepartmentIdAndIntakeIdAndSemesterId(int departmentId, int intakeId, int semesterId);

    Module save(ModuleCreateDTO theModuleCreateDTO);

    Module save(ModuleRegistrationCreateDTO theModuleRegistrationCreateDTO);

    void deleteById(int theId);
}
