package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.SemesterDTO;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.Semester;
import com.ruh.mis.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
