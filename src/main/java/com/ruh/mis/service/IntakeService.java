package com.ruh.mis.service;

import com.ruh.mis.model.DTO.DepartmentCreateDTO;
import com.ruh.mis.model.DTO.DepartmentDTO;
import com.ruh.mis.model.DTO.IntakeCreateDTO;
import com.ruh.mis.model.DTO.IntakeDTO;
import com.ruh.mis.model.Intake;

import java.util.List;

public interface IntakeService {
    List<IntakeDTO> findAll();

    IntakeDTO findById(int theId);

    List<IntakeDTO> getIntakesByDepartmentId(int departmentId);

    Intake save(IntakeCreateDTO theIntakeCreateDTO);

    void deleteById(int theId);

    IntakeDTO update(int intakeId, IntakeCreateDTO intakeCreateDTO);

}
