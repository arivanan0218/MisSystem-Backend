package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.DTO.MarksResponseDTO;
import com.ruh.mis.service.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
public class MarksController {

    @Autowired
    private MarksService marksService;

    // Create marks
    @PostMapping("/create-list")
    public ResponseEntity<Void> addMarks(@RequestBody List<MarksCreateDTO> marksList) {
        marksService.saveMarksList(marksList);
        return ResponseEntity.ok().build();
    }

    // Get marks for a specific student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<MarksResponseDTO> getStudentMarks(@PathVariable int studentId) {
        return ResponseEntity.ok(marksService.getMarksForStudent(studentId));
    }

    // Get all marks
    @GetMapping("/")
    public List<MarksDTO> findAll() {
        return marksService.findAll();
    }

    // Update marks
    @PutMapping("/{id}")
    public ResponseEntity<MarksDTO> updateMarks(@PathVariable int id, @RequestBody MarksCreateDTO marksCreateDTO) {
        MarksDTO updatedMarks = marksService.updateMarks(id, marksCreateDTO);
        return ResponseEntity.ok(updatedMarks);
    }

    // Delete marks
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarks(@PathVariable int id) {
        marksService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}