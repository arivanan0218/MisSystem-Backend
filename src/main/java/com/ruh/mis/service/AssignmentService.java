package com.ruh.mis.service;

import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.Assignment;

import java.util.List;

public interface AssignmentService {
    List<AssignmentDTO> findAll();

    AssignmentDTO findById(int theId);

    List<AssignmentDTO> getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(int departmentId, int intakeId, int semesterId, int moduleId);

    Assignment save(AssignmentCreateDTO theAssignmentCreateDTO);

    void deleteById(int theId);
}
