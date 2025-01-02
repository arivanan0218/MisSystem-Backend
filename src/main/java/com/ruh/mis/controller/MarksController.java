package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.Marks;
import com.ruh.mis.service.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/create")
    public MarksDTO addMarks(@RequestBody MarksCreateDTO theMarksCreateDTO) {
        Marks savedMarks = marksService.save(theMarksCreateDTO);
        return marksService.findById(savedMarks.getId());
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
