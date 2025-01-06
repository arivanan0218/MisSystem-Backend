package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.Marks;
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

    @GetMapping("/")
    public List<MarksDTO> findAll() {
        return marksService.findAll();
    }

    @GetMapping("/{marksId}")
    public MarksDTO getMarks(@PathVariable int marksId) {
        MarksDTO theMarks = marksService.findById(marksId);

        if (theMarks == null) {
            throw new RuntimeException("Marks id not found: " + marksId);
        }

        return theMarks;
    }

    @GetMapping("/assignment/{departmentAndIntakeAndSemesterAndModuleAndAssignmentId}")
    public ResponseEntity<List<MarksDTO>> getMarks(@RequestParam int departmentId,
                                                   @RequestParam int intakeId,
                                                   @RequestParam int semesterId,
                                                   @RequestParam int moduleId,
                                                   @RequestParam int assignmentId) {
        List<MarksDTO> marksDTOS = marksService.getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleAndAssignmentId(departmentId, intakeId, semesterId, moduleId, assignmentId);
        return  ResponseEntity.ok(marksDTOS);
    }


    @PostMapping("/create")
    public MarksDTO addMarks(@RequestBody MarksCreateDTO theMarksCreateDTO) {
        Marks savedMarks = marksService.save(theMarksCreateDTO);
        return marksService.findById(savedMarks.getId());
    }

    @PostMapping("/create-list")
    public ResponseEntity<String> addMarks(@RequestBody List<MarksCreateDTO> marksCreateDTOList) {
        marksService.saveMarksList(marksCreateDTOList);
        return ResponseEntity.ok("Marks list saved successfully!");
    }

    @DeleteMapping("/{marksId}")
    public String deleteMarks(@PathVariable int marksId) {
        MarksDTO tempMarks = marksService.findById(marksId);

        if (tempMarks == null) {
            throw new RuntimeException("Marks id not found: " + marksId);
        }

        marksService.deleteById(marksId);

        return "Deleted marks id: " + marksId;
    }
}
