package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.FinalResultsDTO;
import com.ruh.mis.service.FinalResultsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/final-results")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class FinalResultsController {

    private static final Logger logger = LoggerFactory.getLogger(FinalResultsController.class);

    @Autowired
    private FinalResultsService finalResultsService;

    /**
     * Calculate final results for all students in a department and intake
     *
     * @param departmentId The ID of the department
     * @param intakeId The ID of the intake
     * @return ResponseEntity with success/failure message
     */
    @PostMapping("/calculate/{departmentId}/{intakeId}")
    @PreAuthorize("hasRole('AR') or hasRole('HOD')")
    public ResponseEntity<?> calculateFinalResults(
            @PathVariable int departmentId,
            @PathVariable int intakeId) {

        logger.info("Request to calculate final results for department: {}, intake: {}", 
                departmentId, intakeId);

        try {
            boolean success = finalResultsService.calculateFinalResults(departmentId, intakeId);

            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("message", "Final results calculated successfully");
                response.put("status", true);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Failed to calculate final results. No students or semester results found.");
                response.put("status", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input parameters: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error calculating final results: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred");
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get final results for all students in a department and intake
     *
     * @param departmentId The ID of the department
     * @param intakeId The ID of the intake
     * @return ResponseEntity with list of FinalResultsDTO
     */
    @GetMapping("/{departmentId}/{intakeId}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<?> getFinalResults(
            @PathVariable int departmentId,
            @PathVariable int intakeId) {

        logger.info("Request to get final results for department: {}, intake: {}", 
                departmentId, intakeId);

        try {
            List<FinalResultsDTO> results = finalResultsService.getFinalResults(departmentId, intakeId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving final results: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred");
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get final results for a specific student
     *
     * @param departmentId The ID of the department
     * @param intakeId The ID of the intake
     * @param studentId The ID of the student
     * @return ResponseEntity with FinalResultsDTO
     */
    @GetMapping("/{departmentId}/{intakeId}/student/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<?> getFinalResultsByStudent(
            @PathVariable int departmentId,
            @PathVariable int intakeId,
            @PathVariable int studentId) {

        logger.info("Request to get final results for student: {} in department: {}, intake: {}", 
                studentId, departmentId, intakeId);

        try {
            FinalResultsDTO result = finalResultsService.getFinalResultsByStudent(departmentId, intakeId, studentId);
            
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Final results not found for the student");
                errorResponse.put("status", false);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            logger.error("Unexpected error retrieving final results for student: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred");
            errorResponse.put("status", false);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
