package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.Assignment;
import com.ruh.mis.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/")
    public List<AssignmentDTO> findAll() {
        return assignmentService.findAll();
    }

    @GetMapping("/{assignmentId}")
    public AssignmentDTO getAssignment(@PathVariable int assignmentId) {
        AssignmentDTO theAssignment = assignmentService.findById(assignmentId);

        if (theAssignment == null) {
            throw new RuntimeException("Assignment id not found: " + assignmentId);
        }

        return theAssignment;
    }

    @PostMapping("/create")
    public AssignmentDTO addAssignment(@RequestBody AssignmentCreateDTO theAssignmentCreateDTO) {
        Assignment savedAssignment = assignmentService.save(theAssignmentCreateDTO);
        return assignmentService.findById(savedAssignment.getId());
    }

    @DeleteMapping("/{assignmentId}")
    public String deleteAssignment(@PathVariable int assignmentId) {
        AssignmentDTO tempAssignment = assignmentService.findById(assignmentId);

        if (tempAssignment == null) {
            throw new RuntimeException("Assignment id not found: " + assignmentId);
        }

        assignmentService.deleteById(assignmentId);

        return "Deleted assignment id: " + assignmentId;
    }
}