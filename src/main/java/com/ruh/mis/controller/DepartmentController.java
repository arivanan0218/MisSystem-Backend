package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.DepartmentCreateDTO;
import com.ruh.mis.model.DTO.DepartmentDTO;
import com.ruh.mis.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/")
    public List<DepartmentDTO> findAll() {
        return departmentService.findAll();
    }

    @GetMapping("/{departmentId}")
    public DepartmentDTO getDepartment(@PathVariable int departmentId) {
        DepartmentDTO theDepartment = departmentService.findById(departmentId);

        if (theDepartment == null) {
            throw new RuntimeException("Department id not found: " + departmentId);
        }

        return theDepartment;
    }

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ROLE_LECTURER')")
    public DepartmentDTO addDepartment(@RequestBody DepartmentCreateDTO theDepartmentCreateDTO) {
        return departmentService.findById(departmentService.save(theDepartmentCreateDTO).getId());
    }

    @PutMapping("/{departmentId}") // New endpoint
    public DepartmentDTO updateDepartment(@PathVariable int departmentId, @RequestBody DepartmentCreateDTO departmentCreateDTO) {
        return departmentService.update(departmentId, departmentCreateDTO);
    }

    @DeleteMapping("/{departmentId}")
    public String deleteDepartment(@PathVariable int departmentId) {
        DepartmentDTO tempDepartment = departmentService.findById(departmentId);

        if (tempDepartment == null) {
            throw new RuntimeException("Department id not found: " + departmentId);
        }

        departmentService.deleteById(departmentId);

        return "Deleted department id: " + departmentId;
    }


}
