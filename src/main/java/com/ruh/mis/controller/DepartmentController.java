package com.ruh.mis.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruh.mis.model.DTO.DepartmentCreateDTO;
import com.ruh.mis.model.DTO.DepartmentDTO;
import com.ruh.mis.service.DepartmentService;

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
    // Temporarily disable authorization for testing
    // @PreAuthorize("hasRole('ROLE_AR')")
    public DepartmentDTO addDepartment(@RequestBody DepartmentCreateDTO theDepartmentCreateDTO, HttpServletRequest request) {
        // Log all headers for debugging
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("Department Create - Header: " + headerName + " = " + request.getHeader(headerName));
        }
        return departmentService.findById(departmentService.save(theDepartmentCreateDTO).getId());
    }

    @PutMapping("/{departmentId}")
    // @PreAuthorize("hasRole('ROLE_AR')")
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
