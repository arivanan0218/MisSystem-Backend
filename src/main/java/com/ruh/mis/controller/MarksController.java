package com.ruh.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.DTO.MarksResponseDTO;
import com.ruh.mis.service.MarksService;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class MarksController {

    @Autowired
    private MarksService marksService;

    // Create marks
    @PostMapping("/create-list")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER')")
    public ResponseEntity<?> addMarks(@RequestBody List<MarksCreateDTO> marksList) {
        try {
            if (marksList == null || marksList.isEmpty()) {
                return ResponseEntity.badRequest().body("Marks list cannot be empty");
            }
            
            marksService.saveMarksList(marksList);
            return ResponseEntity.ok().body("Marks saved successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error saving marks: " + e.getMessage());
        }
    }

    // Get marks for a specific student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<MarksResponseDTO> getStudentMarks(@PathVariable int studentId) {
        return ResponseEntity.ok(marksService.getMarksForStudent(studentId));
    }

    // Get marks for a specific assignment
    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER')")
    public ResponseEntity<List<MarksDTO>> getAssignmentMarks(@PathVariable int assignmentId) {
        return ResponseEntity.ok(marksService.findByAssignmentId(assignmentId));
    }

    // Get a specific mark by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER')")
    public ResponseEntity<?> getMark(@PathVariable int id) {
        try {
            MarksDTO mark = marksService.findById(id);
            return ResponseEntity.ok(mark);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get all marks
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER')")
    public ResponseEntity<List<MarksDTO>> findAll() {
        return ResponseEntity.ok(marksService.findAll());
    }

    // Update marks
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER')")
    public ResponseEntity<?> updateMarks(@PathVariable int id, @RequestBody MarksCreateDTO marksCreateDTO) {
        try {
            if (marksCreateDTO == null) {
                return ResponseEntity.badRequest().body("Marks data cannot be null");
            }
            
            MarksDTO updatedMarks = marksService.updateMarks(id, marksCreateDTO);
            return ResponseEntity.ok(updatedMarks);
        } catch (RuntimeException e) {
            // This will catch both IllegalArgumentException (which is a subclass of RuntimeException)
            // and other RuntimeExceptions like "Marks not found"
            return ResponseEntity.status(
                e instanceof IllegalArgumentException ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND
            ).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating marks: " + e.getMessage());
        }
    }

    // Delete marks
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD')")
    public ResponseEntity<Void> deleteMarks(@PathVariable int id) {
        marksService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}