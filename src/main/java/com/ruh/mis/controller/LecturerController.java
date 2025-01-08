package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.LecturerDTO;
import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.Lecturer;
import com.ruh.mis.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping("/")
    public List<LecturerDTO> findAll() {
        return lecturerService.findAll();
    }

    @GetMapping("/{lecturerId}")
    public LecturerDTO getLecturer(@PathVariable int lecturerId) {
        LecturerDTO theLecturer = lecturerService.findById(lecturerId);

        if (theLecturer == null) {
            throw new RuntimeException("Lecturer id not found: " + lecturerId);
        }

        return theLecturer;
    }

    @PostMapping("/create")
    public LecturerDTO addLecturer(@RequestBody LecturerCreateDTO theLecturerCreateDTO) {
        Lecturer savedLecturer = lecturerService.save(theLecturerCreateDTO);
        return lecturerService.findById(savedLecturer.getId());
    }

    @PostMapping("create-list")
    public ResponseEntity<String> addLecturer(@RequestBody List<LecturerCreateDTO> lecturerCreateDTOList) {
        lecturerService.saveLecturersList(lecturerCreateDTOList);
        return ResponseEntity.ok("Lecturer List saved successfully");
    }

    @DeleteMapping("/{lecturerId}")
    public String deleteLecturer(@PathVariable int lecturerId) {
        LecturerDTO tempLecturer = lecturerService.findById(lecturerId);

        if (tempLecturer == null) {
            throw new RuntimeException("Lecturer id not found: " + lecturerId);
        }

        lecturerService.deleteById(lecturerId);

        return "Deleted lecturer id: " + lecturerId;
    }
}
