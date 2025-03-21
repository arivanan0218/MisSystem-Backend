package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.ModuleResultDTO;
import com.ruh.mis.service.ModuleResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module-results")
public class ModuleResultController {

    @Autowired
    private ModuleResultService moduleResultService;

    @PostMapping("/calculate")
    public ResponseEntity<Void> calculateResults(
            @RequestParam int departmentId,
            @RequestParam int intakeId,
            @RequestParam int semesterId,
            @RequestParam int moduleId
    ) {
        moduleResultService.calculateModuleResults(departmentId, intakeId, semesterId, moduleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ModuleResultDTO>> getResults(
            @RequestParam int departmentId,
            @RequestParam int intakeId,
            @RequestParam int semesterId,
            @RequestParam int moduleId
    ) {
        List<ModuleResultDTO> results = moduleResultService.getModuleResults(
                departmentId, intakeId, semesterId, moduleId);
        return ResponseEntity.ok(results);
    }
}