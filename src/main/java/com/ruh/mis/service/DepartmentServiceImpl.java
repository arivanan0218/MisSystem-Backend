package com.ruh.mis.service;

import com.ruh.mis.model.DTO.DepartmentCreateDTO;
import com.ruh.mis.model.DTO.DepartmentDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<DepartmentDTO> findAll() {
        return departmentRepository.findAll().stream()
                .map(department -> modelMapper.map(department, DepartmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO findById(int theId) {
        Department department = modelMapper.map(departmentRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Department not found: " + theId)), Department.class);
        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public Department save(DepartmentCreateDTO theDepartmentCreateDTO) {
        Department department = modelMapper.map(theDepartmentCreateDTO, Department.class);
        return departmentRepository.save(department);
    }

    @Override
    public void deleteById(int theId) {
        departmentRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public DepartmentDTO update(int departmentId, DepartmentCreateDTO departmentCreateDTO) {
        // Find the existing department
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));

        // Update the fields
        existingDepartment.setDepartmentName(departmentCreateDTO.getDepartmentName());
        existingDepartment.setDepartmentCode(departmentCreateDTO.getDepartmentCode());
        existingDepartment.setHodName(departmentCreateDTO.getHodName());

        // Save the updated entity
        Department updatedDepartment = departmentRepository.save(existingDepartment);

        // Map the updated entity to DTO and return
        return modelMapper.map(updatedDepartment, DepartmentDTO.class);
    }
}
