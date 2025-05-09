package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.TranscriptDTO;
import com.ruh.mis.service.TranscriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/transcripts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TranscriptController {

    private static final Logger logger = LoggerFactory.getLogger(TranscriptController.class);

    @Autowired
    private TranscriptService transcriptService;

    /**
     * Generate a transcript for a student
     * 
     * @param studentId The ID of the student
     * @return The transcript containing all student and module information
     */
    @GetMapping("/student/{studentId}")
    // Temporarily remove role-based authorization to test the endpoint
    //@PreAuthorize("hasRole('AR') or hasRole('HOD') or hasRole('STUDENT')")
    public ResponseEntity<?> generateTranscript(@PathVariable int studentId) {
        logger.info("Generating transcript for student with ID: {}", studentId);
        
        try {
            TranscriptDTO transcript = transcriptService.generateTranscript(studentId);
            return ResponseEntity.ok(transcript);
        } catch (Exception e) {
            logger.error("Error generating transcript for student with ID: {}", studentId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating transcript: " + e.getMessage());
        }
    }
    
    /**
     * Public endpoint for testing transcript generation without authentication
     * 
     * @param studentId The ID of the student
     * @return The transcript containing all student and module information
     */
    @GetMapping("/public/student/{studentId}")
    public ResponseEntity<?> generateTranscriptPublic(@PathVariable int studentId) {
        logger.info("Generating transcript (public endpoint) for student with ID: {}", studentId);
        
        try {
            TranscriptDTO transcript = transcriptService.generateTranscript(studentId);
            return ResponseEntity.ok(transcript);
        } catch (Exception e) {
            logger.error("Error generating transcript (public endpoint) for student with ID: {}", studentId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating transcript: " + e.getMessage());
        }
    }
    
    /**
     * Test endpoint that bypasses Spring Security completely
     * 
     * @param studentId The ID of the student
     * @return The transcript containing all student and module information
     */
    @GetMapping("/test/student/{studentId}")
    public ResponseEntity<?> generateTranscriptTest(@PathVariable int studentId) {
        logger.info("Generating transcript (test endpoint) for student with ID: {}", studentId);
        
        try {
            TranscriptDTO transcript = transcriptService.generateTranscript(studentId);
            return ResponseEntity.ok(transcript);
        } catch (Exception e) {
            logger.error("Error generating transcript (test endpoint) for student with ID: {}", studentId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating transcript: " + e.getMessage());
        }
    }
}
