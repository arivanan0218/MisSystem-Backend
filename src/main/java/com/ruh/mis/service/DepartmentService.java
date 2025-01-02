package com.ruh.mis.service;

import com.ruh.mis.model.DTO.DepartmentCreateDTO;
import com.ruh.mis.model.DTO.DepartmentDTO;
import com.ruh.mis.model.Department;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> findAll();

    DepartmentDTO findById(int theId);

    Department save(DepartmentCreateDTO theDepartmentCreateDTO);

    void deleteById(int theId);

    DepartmentDTO update(int departmentId, DepartmentCreateDTO departmentCreateDTO);

}
