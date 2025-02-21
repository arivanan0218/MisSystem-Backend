package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.EndExamCreateDTO;
import com.ruh.mis.model.DTO.EndExamDTO;
import com.ruh.mis.service.EndExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/endexam")
public class EndExamController {

    @Autowired private EndExamService endExamService;

    @GetMapping("/")
    public List<EndExamDTO> getAllEndExams() {
        return endExamService.findAll();
    }

    @GetMapping("/{id}")
    public EndExamDTO getEndExamById(@PathVariable int id) {
        return endExamService.findById(id);
    }

    @GetMapping("/module")
    public ResponseEntity<List<EndExamDTO>> getEndExamByModule(
            @RequestParam int departmentId,
            @RequestParam int intakeId,
            @RequestParam int semesterId,
            @RequestParam int moduleId) {
        List<EndExamDTO> exams = endExamService
                .getEndExamByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                        departmentId, intakeId, semesterId, moduleId);
        return ResponseEntity.ok(exams);
    }

    @PostMapping("/create")
    public EndExamDTO createEndExam(@RequestBody EndExamCreateDTO dto) {
        return endExamService.save(dto);
    }

    @PutMapping("/{id}")
    public EndExamDTO updateEndExam(@PathVariable int id, @RequestBody EndExamCreateDTO dto) {
        return endExamService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteEndExam(@PathVariable int id) {
        endExamService.deleteById(id);
        return "Deleted EndExam with ID: " + id;
    }
}