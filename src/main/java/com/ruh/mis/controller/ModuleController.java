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
        Module savedModule = moduleService.save(theModuleCreateDTO);
        return moduleService.findById(savedModule.getId());
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

    @PutMapping("/{moduleId}") // New endpoint
    public ModuleDTO updateModule(@PathVariable int moduleId, @RequestBody ModuleCreateDTO moduleCreateDTO) {
        return moduleService.update(moduleId, moduleCreateDTO);
    }
}
