package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.model.Module;

import java.util.List;

public interface ModuleService {
    List<ModuleDTO> findAll();

    ModuleDTO findById(int theId);

    Module save(ModuleCreateDTO theModuleCreateDTO);

    void deleteById(int theId);
}