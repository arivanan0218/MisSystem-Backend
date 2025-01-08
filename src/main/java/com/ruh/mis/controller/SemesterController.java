package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.SemesterDTO;
import com.ruh.mis.model.Semester;
import com.ruh.mis.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semester")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @GetMapping("/")
    public List<SemesterDTO> findAll() {
        return semesterService.findAll();
    }

    @GetMapping("/{semesterId}")
    public SemesterDTO getSemester(@PathVariable int semesterId) {
        SemesterDTO theSemester = semesterService.findById(semesterId);

        if (theSemester == null) {
            throw new RuntimeException("Semester id not found: " + semesterId);
        }

        return theSemester;
    }

    @GetMapping("/intake/{departmentAndIntakeId}")
    public ResponseEntity<List<SemesterDTO>> getSemesters(@RequestParam int departmentId,
                                                          @RequestParam int intakeId) {
        List<SemesterDTO> semesters = semesterService.getSemestersByDepartmentIdAndIntakeId(departmentId, intakeId);
        return ResponseEntity.ok(semesters);
    }

    @PostMapping("/create")
    public SemesterDTO addSemester(@RequestBody SemesterCreateDTO theSemesterCreateDTO) {
        Semester savedSemester = semesterService.save(theSemesterCreateDTO);
        return semesterService.findById(savedSemester.getId());
    }

    @DeleteMapping("/{semesterId}")
    public String deleteSemester(@PathVariable int semesterId) {
        SemesterDTO tempSemester = semesterService.findById(semesterId);

        if (tempSemester == null) {
            throw new RuntimeException("Semester id not found: " + semesterId);
        }

        semesterService.deleteById(semesterId);

        return "Deleted semester id: " + semesterId;
    }

    @PutMapping("/{semesterId}") // New endpoint
    public SemesterDTO updateSemester(@PathVariable int semesterId, @RequestBody SemesterCreateDTO semesterCreateDTO) {
        return semesterService.update(semesterId, semesterCreateDTO);
    }
}
