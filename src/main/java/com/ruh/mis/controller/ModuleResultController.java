package com.ruh.mis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruh.mis.model.DTO.ModuleResultDTO;
import com.ruh.mis.service.ModuleResultService;
import com.ruh.mis.service.ModuleResultServiceImpl;

@RestController
@RequestMapping("/api/module-results")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class ModuleResultController {

    @Autowired
    private ModuleResultService moduleResultService;
    
    @PostMapping("/calculate")
    @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR')")
    public ResponseEntity<Map<String, Object>> calculateResults(
            @RequestParam int departmentId,
            @RequestParam int intakeId,
            @RequestParam int semesterId,
            @RequestParam int moduleId
    ) {
        moduleResultService.calculateModuleResults(departmentId, intakeId, semesterId, moduleId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Module results calculated successfully");
        response.put("status", true);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student-module")
    @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<List<ModuleResultDTO>> getStudentModuleResults(
            @RequestParam int departmentId,
            @RequestParam int intakeId,
            @RequestParam int semesterId,
            @RequestParam int moduleId,
            @RequestParam int studentId
    ) {
        List<ModuleResultDTO> results = moduleResultService.getModuleResults(
                departmentId, intakeId, semesterId, moduleId, studentId);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/module")
    @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<List<ModuleResultDTO>> getModuleResults(
            @RequestParam int departmentId,
            @RequestParam int intakeId,
            @RequestParam int semesterId,
            @RequestParam int moduleId
    ) {
        List<ModuleResultDTO> results = moduleResultService.getModuleResults(
                departmentId, intakeId, semesterId, moduleId);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/semester-student")
    @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<List<ModuleResultDTO>> getSemesterStudentResults(
            @RequestParam int departmentId,
            @RequestParam int intakeId,
            @RequestParam int semesterId,
            @RequestParam int studentId
    ) {
        List<ModuleResultDTO> results = moduleResultService.getSemesterStudentResults(
                departmentId, intakeId, semesterId, studentId);
        return ResponseEntity.ok(results);
    }
    
    @PostMapping("/update-statuses")
    @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR')")
    public ResponseEntity<Map<String, Object>> updateAllModuleResultStatuses() {
        ((ModuleResultServiceImpl) moduleResultService).updateAllModuleResultStatuses();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All module result statuses have been updated");
        response.put("status", true);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/update-status/{resultId}")
    @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR')")
    public ResponseEntity<Map<String, Object>> updateModuleResultStatus(
            @PathVariable int resultId,
            @RequestBody Map<String, String> statusUpdate
    ) {
        String newStatus = statusUpdate.get("newStatus");
        boolean success = moduleResultService.updateModuleResultStatus(resultId, newStatus);
        
        Map<String, Object> response = new HashMap<>();
        
        if (success) {
            response.put("message", "Module result status updated successfully");
            response.put("status", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to update module result status");
            response.put("status", false);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
//     @GetMapping("/student/{studentId}")
//     @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
//     public ResponseEntity<List<ModuleResultDTO>> getStudentResults(
//             @PathVariable int studentId
//     ) {
//         List<ModuleResultDTO> results = moduleResultService.getStudentModuleResults(
//                 studentId, null, null, null, null);
//         return ResponseEntity.ok(results);
//     }
    
//     @GetMapping("/student/{studentId}/filtered")
//     @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
//     public ResponseEntity<List<ModuleResultDTO>> getFilteredStudentResults(
//             @PathVariable int studentId,
//             @RequestParam(required = false) Integer departmentId,
//             @RequestParam(required = false) Integer intakeId,
//             @RequestParam(required = false) Integer semesterId,
//             @RequestParam(required = false) Integer moduleId
//     ) {
//         List<ModuleResultDTO> results = moduleResultService.getStudentModuleResults(
//                 studentId, departmentId, intakeId, semesterId, moduleId);
//         return ResponseEntity.ok(results);
//     }
}