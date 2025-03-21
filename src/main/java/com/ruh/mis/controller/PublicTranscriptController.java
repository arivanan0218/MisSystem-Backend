package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.TranscriptDTO;
import com.ruh.mis.service.TranscriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for accessing transcripts without authentication
 * This is a separate controller from TranscriptController to ensure
 * that security configuration can be applied specifically to these endpoints
 */
@RestController
@RequestMapping("/public/transcripts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicTranscriptController {

    private static final Logger logger = LoggerFactory.getLogger(PublicTranscriptController.class);

    @Autowired
    private TranscriptService transcriptService;

    /**
     * Generate a transcript for a student without requiring authentication
     * 
     * @param studentId The ID of the student
     * @return The transcript containing all student and module information
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> generateTranscript(@PathVariable int studentId) {
        logger.info("Generating transcript (public controller) for student with ID: {}", studentId);
        
        try {
            TranscriptDTO transcript = transcriptService.generateTranscript(studentId);
            return ResponseEntity.ok(transcript);
        } catch (Exception e) {
            logger.error("Error generating transcript (public controller) for student with ID: {}", studentId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating transcript: " + e.getMessage());
        }
    }
}
