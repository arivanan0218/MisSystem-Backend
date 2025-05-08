package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.IntakeCreateDTO;
import com.ruh.mis.model.DTO.IntakeDTO;
import com.ruh.mis.model.Intake;
import com.ruh.mis.service.IntakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intake")
public class IntakeController {

    @Autowired
    private IntakeService intakeService;

    @GetMapping("/")
    public List<IntakeDTO> findAll() {
        return intakeService.findAll();
    }

    @GetMapping("/{intakeId}")
    public IntakeDTO getIntake(@PathVariable int intakeId) {
        IntakeDTO theIntake = intakeService.findById(intakeId);

        if (theIntake == null) {
            throw new RuntimeException("Intake id not found: " + intakeId);
        }

        return theIntake;
    }

    // Get Intakes by Department ID
    @GetMapping("/department/{departmentId}")
    public List<IntakeDTO> getIntakesByDepartmentId(@PathVariable int departmentId) {
        return intakeService.getIntakesByDepartmentId(departmentId);  // Returns List of DTOs
    }

    @PostMapping("/create")
    public IntakeDTO addIntake(@RequestBody IntakeCreateDTO theIntakeCreateDTO) {
        // Save the intake
        Intake savedIntake = intakeService.save(theIntakeCreateDTO);

        // Get the full intake DTO with all relationships
        IntakeDTO intakeDTO = intakeService.findById(savedIntake.getId());

        // Ensure the department ID from the create DTO is explicitly set in the return DTO
        intakeDTO.setDepartmentId(theIntakeCreateDTO.getDepartmentId());

        return intakeDTO;
    }

    @PutMapping("/{intakeId}") // New endpoint
    public IntakeDTO updateIntake(@PathVariable int intakeId, @RequestBody IntakeCreateDTO intakeCreateDTO) {
        return intakeService.update(intakeId, intakeCreateDTO);
    }

    @DeleteMapping("/{intakeId}")
    public String deleteIntake(@PathVariable int intakeId) {
        IntakeDTO tempIntake = intakeService.findById(intakeId);

        if (tempIntake == null) {
            throw new RuntimeException("Intake id not found: " + intakeId);
        }

        intakeService.deleteById(intakeId);

        return "Deleted intake id: " + intakeId;
    }
}