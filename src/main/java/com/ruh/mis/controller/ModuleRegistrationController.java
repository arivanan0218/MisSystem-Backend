package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.service.ModuleRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module-registrations")
public class ModuleRegistrationController {

    @Autowired
    private ModuleRegistrationService moduleRegistrationService;

    @GetMapping("/")
    public ResponseEntity<List<ModuleRegistrationDTO>> getAllModuleRegistrations() {
        List<ModuleRegistrationDTO> moduleRegistrations = moduleRegistrationService.findAll();
        return ResponseEntity.ok(moduleRegistrations);
    }

    @PostMapping("/create-list")
    public ResponseEntity<Void> createModuleRegistrations(@RequestBody List<ModuleRegistrationCreateDTO> moduleRegistrationCreateDTOList) {
        moduleRegistrationService.saveModuleRegistrationList(moduleRegistrationCreateDTOList);
        return ResponseEntity.ok().build();
    }
}
