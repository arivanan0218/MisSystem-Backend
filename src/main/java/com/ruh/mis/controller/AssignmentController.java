package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.*;
import com.ruh.mis.model.Assignment;
import com.ruh.mis.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/module/{departmentAndIntakeAndSemesterAndModuleId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignments(@RequestParam int departmentId,
                                                              @RequestParam int intakeId,
                                                              @RequestParam int semesterId,
                                                              @RequestParam int moduleId) {
        List<AssignmentDTO> assignmentDTOS = assignmentService.getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(departmentId, intakeId, semesterId, moduleId);
        return  ResponseEntity.ok(assignmentDTOS);
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

    @PutMapping("/{assignmentId}") // New endpoint
    public AssignmentDTO updateAssignment(@PathVariable int assignmentId, @RequestBody AssignmentCreateDTO assignmentCreateDTO) {
        return assignmentService.update(assignmentId, assignmentCreateDTO);
    }

    @PostMapping("/create-marks-list")
    public ResponseEntity<Void> createMarks(@RequestBody List<AssignmentMarksCreateDTO> assignmentMarksCreateDTOList) {
        assignmentService.saveAssignmentMarksList(assignmentMarksCreateDTOList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/marks-list")
    public ResponseEntity<List<AssignmentMarksDTO>> getMarks() {
        List<AssignmentMarksDTO> assignmentMarks = assignmentService.findAllMarks();
        return ResponseEntity.ok(assignmentMarks);
    }
}
