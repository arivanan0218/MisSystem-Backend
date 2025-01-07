package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.*;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.Semester;
import com.ruh.mis.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/moduleRegistration")
    public List<ModuleRegistrationDTO> findAllRegistrations() {
        return moduleService.findAllRegistration();
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

    @GetMapping("/moduleRegistration/{departmentAndIntakeAndSemesterId}")
    public ResponseEntity<List<ModuleRegistrationDTO>> getModuleRegistrations(@RequestParam int departmentId,
                                                                              @RequestParam int intakeId,
                                                                              @RequestParam int semesterId) {
        List<ModuleRegistrationDTO> moduleRegistrationDTOS = moduleService.getModuleRegistrationByDepartmentIdAndIntakeIdAndSemesterId(departmentId, intakeId, semesterId);
        return ResponseEntity.ok(moduleRegistrationDTOS);
    }

    @PostMapping("/create")
    public ModuleDTO addModule(@RequestBody ModuleCreateDTO theModuleCreateDTO) {
        Module savedModule = moduleService.save(theModuleCreateDTO);
        return moduleService.findById(savedModule.getId());
    }

    @PostMapping("/create/moduleRegistration")
    public ModuleRegistrationDTO addModuleRegistration(@RequestBody ModuleRegistrationCreateDTO theModuleRegistrationCreateDTO) {
        Module savedModuleRegistration = moduleService.save(theModuleRegistrationCreateDTO);
        return moduleService.findByRegistrationId(savedModuleRegistration.getId());
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
