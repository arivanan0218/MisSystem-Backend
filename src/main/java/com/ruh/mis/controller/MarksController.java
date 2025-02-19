package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.MarksCreateDTO;
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

    @PostMapping("/create-list")
    public ResponseEntity<Void> addMarks(@RequestBody List<MarksCreateDTO> marksList) {
        marksService.saveMarksList(marksList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<MarksResponseDTO> getStudentMarks(@PathVariable int studentId) {
        return ResponseEntity.ok(marksService.getMarksForStudent(studentId));
    }
}
