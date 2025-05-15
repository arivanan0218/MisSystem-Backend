package com.ruh.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.model.Module;
import com.ruh.mis.service.ModuleService;

@RestController
@RequestMapping("/api/module")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @GetMapping("/")
    public List<ModuleDTO> findAll() {
        return moduleService.findAll();
    }

    @GetMapping("/{moduleId}")
    public ModuleDTO getModule(@PathVariable int moduleId) {
        ModuleDTO theModule = moduleService.findById(moduleId);

        if (theModule == null) {
            throw new RuntimeException("Module id not found: " + moduleId);
        }

        return theModule;
    }

    @GetMapping("/semester/{departmentAndIntakeAndSemesterId}")
    public ResponseEntity<List<ModuleDTO>> getModules(@RequestParam int departmentId,
                                                      @RequestParam int intakeId,
                                                      @RequestParam int semesterId) {
        List<ModuleDTO> modules = moduleService.getModuleByDepartmentIdAndIntakeIdAndSemesterId(departmentId, intakeId, semesterId);
        return ResponseEntity.ok(modules);
    }

    @PostMapping("/create")
    public ModuleDTO addModule(@RequestBody ModuleCreateDTO theModuleCreateDTO) {
        // Save the module
        Module savedModule = moduleService.save(theModuleCreateDTO);

        // Get the full module DTO with all relationships (department, intake, semester)
        ModuleDTO moduleDTO = moduleService.findById(savedModule.getId());

        // Ensure the IDs from the create DTO are explicitly set in the return DTO
        moduleDTO.setDepartmentId(theModuleCreateDTO.getDepartmentId());
        moduleDTO.setIntakeId(theModuleCreateDTO.getIntakeId());
        moduleDTO.setSemesterId(theModuleCreateDTO.getSemesterId());

        return moduleDTO;
    }

    @DeleteMapping("/{moduleId}")
    public String deleteModule(@PathVariable int moduleId) {
        ModuleDTO tempModule = moduleService.findById(moduleId);

        if (tempModule == null) {
            throw new RuntimeException("Module id not found: " + moduleId);
        }

        moduleService.deleteById(moduleId);

        return "Deleted module id: " + moduleId;
    }

    @PutMapping("/{moduleId}")
    public ModuleDTO updateModule(@PathVariable int moduleId, @RequestBody ModuleCreateDTO moduleCreateDTO) {
        // Ensure IDs are preserved during update
        ModuleDTO existingModule = moduleService.findById(moduleId);
        if (existingModule == null) {
            throw new RuntimeException("Module id not found: " + moduleId);
        }
        
        // If missing, use the existing values for the IDs
        if (moduleCreateDTO.getDepartmentId() == 0) {
            moduleCreateDTO.setDepartmentId(existingModule.getDepartmentId());
        }
        if (moduleCreateDTO.getIntakeId() == 0) {
            moduleCreateDTO.setIntakeId(existingModule.getIntakeId());
        }
        if (moduleCreateDTO.getSemesterId() == 0) {
            moduleCreateDTO.setSemesterId(existingModule.getSemesterId());
        }
        
        return moduleService.update(moduleId, moduleCreateDTO);
    }
}