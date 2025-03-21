package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ModuleResultDTO;
import java.util.List;

public interface ModuleResultService {
    void calculateModuleResults(int departmentId, int intakeId, int semesterId, int moduleId);
    List<ModuleResultDTO> getModuleResults(int departmentId, int intakeId, int semesterId, int moduleId);
}