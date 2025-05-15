package com.ruh.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruh.mis.model.DTO.AdminRegistrationUpdateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;
import com.ruh.mis.service.ModuleRegistrationService;

@RestController
@RequestMapping("/api/module-registration")
public class ModuleRegistrationController {

    @Autowired
    private ModuleRegistrationService registrationService;

    // Student endpoints
    
    @PostMapping
    public ResponseEntity<String> registerModules(@RequestBody ModuleRegistrationRequestDTO requestDTO) {
        try {
            registrationService.registerModules(requestDTO);
            return ResponseEntity.ok("Modules registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/student")
    public ResponseEntity<ModuleRegistrationResponseDTO> getRegistrationsForStudent(
            @RequestParam int studentId,
            @RequestParam int semesterId,
            @RequestParam int intakeId,
            @RequestParam int departmentId) {
        try {
            return ResponseEntity.ok(registrationService.getRegistrationDetailsForStudent(
                    studentId, semesterId, intakeId, departmentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Admin endpoints
    
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<ModuleRegistrationDTO>> getStudentsForModule(@PathVariable int moduleId) {
        try {
            List<ModuleRegistrationDTO> registrations = registrationService.getRegistrationsByModuleId(moduleId);
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<ModuleRegistrationDTO>> getPendingRegistrations(
            @RequestParam int departmentId,
            @RequestParam int intakeId,
            @RequestParam int semesterId) {
        try {
            List<ModuleRegistrationDTO> registrations = registrationService
                    .getPendingRegistrationsByDepartmentIntakeSemester(departmentId, intakeId, semesterId);
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/pending/module")
    public ResponseEntity<List<ModuleRegistrationDTO>> getPendingRegistrationsByModule(
            @RequestParam int moduleId,
            @RequestParam int semesterId,
            @RequestParam int intakeId,
            @RequestParam int departmentId) {
        try {
            List<ModuleRegistrationDTO> registrations = registrationService
                    .getPendingRegistrationsByModule(moduleId, semesterId, intakeId, departmentId);
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateRegistrationStatus(@RequestBody AdminRegistrationUpdateDTO updateDTO) {
        try {
            ModuleRegistrationDTO updatedRegistration = registrationService.updateRegistrationStatus(updateDTO);
            return ResponseEntity.ok(updatedRegistration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }
}