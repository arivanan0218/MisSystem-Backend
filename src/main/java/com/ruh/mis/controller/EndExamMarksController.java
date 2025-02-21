package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.*;
import com.ruh.mis.service.EndExamMarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/endexam-marks")
public class EndExamMarksController {

    @Autowired
    private EndExamMarksService endExamMarksService;

    @GetMapping("/")
    public List<EndExamMarksDTO> findAll() {
        return endExamMarksService.findAll();
    }

    @PostMapping("/create-list")
    public ResponseEntity<Void> addEndExamMarks(@RequestBody List<EndExamMarksCreateDTO> marksList) {
        endExamMarksService.saveEndExamMarksList(marksList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EndExamMarksDTO>> getEndExamMarksForStudent(@PathVariable int studentId) {
        return ResponseEntity.ok(endExamMarksService.getEndExamMarksForStudent(studentId));
    }

    @GetMapping("/module-marks/{studentId}/{moduleId}")
    public ResponseEntity<ModuleMarksResponseDTO> getFinalModuleMarks(
            @PathVariable int studentId,
            @PathVariable int moduleId) {
        ModuleMarksResponseDTO finalModuleMarks = endExamMarksService.calculateFinalModuleMarks(studentId, moduleId);
        return ResponseEntity.ok(finalModuleMarks);
    }

    @PutMapping("/update")
    public ResponseEntity<EndExamMarksDTO> updateEndExamMarks(@RequestBody EndExamMarksDTO marksDTO) {
        return ResponseEntity.ok(endExamMarksService.updateEndExamMarks(marksDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndExamMarks(@PathVariable int id) {
        endExamMarksService.deleteEndExamMarksById(id);
        return ResponseEntity.ok().build();
    }
}