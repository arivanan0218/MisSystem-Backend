package com.ruh.mis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruh.mis.model.DTO.SemesterResultsDTO;
import com.ruh.mis.service.SemesterResultsService;

@RestController
@RequestMapping("/api/semester-results")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class SemesterResultsController {

    private static final Logger logger = LoggerFactory.getLogger(SemesterResultsController.class);

    @Autowired
    private SemesterResultsService semesterResultsService;

    @PostMapping("/calculate/{departmentId}/{intakeId}/{semesterId}")
    @PreAuthorize("hasAuthority('ROLE_AR') or hasAuthority('ROLE_HOD')")
    public ResponseEntity<?> calculateSemesterResults(
            @PathVariable int departmentId,
            @PathVariable int intakeId,
            @PathVariable int semesterId) {
        
        try {
            logger.info("Calculating semester results for department: {}, intake: {}, semester: {}", 
                    departmentId, intakeId, semesterId);
            
            semesterResultsService.calculateSemesterResults(departmentId, intakeId, semesterId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Semester results calculated successfully");
            response.put("status", true);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error calculating semester results: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error calculating semester results: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred");
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{departmentId}/{intakeId}/{semesterId}/{studentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<?> getSemesterResultsByStudent(
            @PathVariable int departmentId,
            @PathVariable int intakeId,
            @PathVariable int semesterId,
            @PathVariable int studentId) {
        
        try {
            logger.info("Retrieving semester results for student: {} in department: {}, intake: {}, semester: {}", 
                    studentId, departmentId, intakeId, semesterId);
            
            SemesterResultsDTO results = semesterResultsService.getSemesterResultsByStudent(
                    departmentId, intakeId, semesterId, studentId);
            
            return ResponseEntity.ok(results);
        } catch (RuntimeException e) {
            logger.error("Error retrieving semester results for student: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving semester results for student: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred");
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{departmentId}/{intakeId}/{semesterId}")
    @PreAuthorize("hasAnyAuthority('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<?> getSemesterResults(
            @PathVariable int departmentId,
            @PathVariable int intakeId,
            @PathVariable int semesterId) {
        
        try {
            logger.info("Retrieving semester results for department: {}, intake: {}, semester: {}", 
                    departmentId, intakeId, semesterId);
            
            List<SemesterResultsDTO> results = semesterResultsService.getSemesterResults(
                    departmentId, intakeId, semesterId);
            
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input parameters: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving semester results: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred");
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
