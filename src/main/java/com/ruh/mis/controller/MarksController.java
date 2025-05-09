package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.DTO.MarksResponseDTO;
import com.ruh.mis.service.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class MarksController {

    @Autowired
    private MarksService marksService;

    // Create marks
    @PostMapping("/create-list")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER')")
    public ResponseEntity<Void> addMarks(@RequestBody List<MarksCreateDTO> marksList) {
        marksService.saveMarksList(marksList);
        return ResponseEntity.ok().build();
    }

    // Get marks for a specific student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER', 'ROLE_STUDENT')")
    public ResponseEntity<MarksResponseDTO> getStudentMarks(@PathVariable int studentId) {
        return ResponseEntity.ok(marksService.getMarksForStudent(studentId));
    }

    // Get all marks
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER')")
    public List<MarksDTO> findAll() {
        return marksService.findAll();
    }

    // Update marks
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD', 'ROLE_MODULE_COORDINATOR', 'ROLE_LECTURER')")
    public ResponseEntity<MarksDTO> updateMarks(@PathVariable int id, @RequestBody MarksCreateDTO marksCreateDTO) {
        MarksDTO updatedMarks = marksService.updateMarks(id, marksCreateDTO);
        return ResponseEntity.ok(updatedMarks);
    }

    // Delete marks
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_AR', 'ROLE_HOD')")
    public ResponseEntity<Void> deleteMarks(@PathVariable int id) {
        marksService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}