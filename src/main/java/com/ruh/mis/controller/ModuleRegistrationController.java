package com.ruh.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;
import com.ruh.mis.service.ModuleRegistrationService;

@RestController
@RequestMapping("/api/module-registration")
public class ModuleRegistrationController {

    @Autowired
    private ModuleRegistrationService registrationService;

    @PostMapping
    public ResponseEntity<String> registerModules(@RequestBody ModuleRegistrationRequestDTO requestDTO) {
        registrationService.registerModules(requestDTO);
        return ResponseEntity.ok("Modules registered successfully!");
    }

    // Specific endpoint using query parameters instead of path variables
    @GetMapping("/student")
    public ResponseEntity<ModuleRegistrationResponseDTO> getRegistrationsForStudent(
            @RequestParam int studentId,
            @RequestParam int semesterId,
            @RequestParam int intakeId,
            @RequestParam int departmentId) {
        return ResponseEntity.ok(registrationService.getRegistrationDetailsForStudent(
                studentId, semesterId, intakeId, departmentId));
    }
    
    // Add this new endpoint to retrieve students registered for a specific module
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<ModuleRegistrationDTO>> getStudentsForModule(@PathVariable int moduleId) {
        List<ModuleRegistrationDTO> registrations = registrationService.getRegistrationsByModuleId(moduleId);
        return ResponseEntity.ok(registrations);
    }
}