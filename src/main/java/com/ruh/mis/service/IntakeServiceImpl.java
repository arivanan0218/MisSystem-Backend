package com.ruh.mis.service;

import com.ruh.mis.model.DTO.IntakeCreateDTO;
import com.ruh.mis.model.DTO.IntakeDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.IntakeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntakeServiceImpl implements IntakeService{

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<IntakeDTO> findAll() {
        return intakeRepository.findAll().stream()
                .map(intake -> {
                    IntakeDTO dto = modelMapper.map(intake, IntakeDTO.class);

                    // Explicitly set entity IDs
                    if (intake.getDepartment() != null) {
                        dto.setDepartmentId(intake.getDepartment().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public IntakeDTO findById(int theId) {
        Intake intake = intakeRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Intake not found: " + theId));

        // Map entity to DTO
        IntakeDTO dto = modelMapper.map(intake, IntakeDTO.class);

        // Explicitly set entity IDs
        if (intake.getDepartment() != null) {
            dto.setDepartmentId(intake.getDepartment().getId());
        }

        return dto;
    }

    @Override
    public List<IntakeDTO> getIntakesByDepartmentId(int departmentId) {
        List<Intake> intakes = intakeRepository.findByDepartmentId(departmentId);

        // Use ModelMapper to map List<Intake> to List<IntakeDTO> with explicit ID handling
        return intakes.stream()
                .map(intake -> {
                    IntakeDTO dto = modelMapper.map(intake, IntakeDTO.class);

                    // Explicitly set entity IDs
                    if (intake.getDepartment() != null) {
                        dto.setDepartmentId(intake.getDepartment().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Intake save(IntakeCreateDTO theIntakeCreateDTO) {
        // Create a new Intake entity
        Intake intake = modelMapper.map(theIntakeCreateDTO, Intake.class);

        // Fetch the required Department entity from repository using its ID from the DTO
        Department department = departmentRepository.findById(theIntakeCreateDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found: " + theIntakeCreateDTO.getDepartmentId()));

        // Set the relationship manually
        intake.setDepartment(department);

        // Save the intake
        return intakeRepository.save(intake);
    }

    @Override
    @Transactional
    public IntakeDTO update(int intakeId, IntakeCreateDTO intakeCreateDTO) {
        // Find the existing department
        Intake existingIntake = intakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found: " + intakeId));

        // Update the fields
        existingIntake.setIntakeYear(intakeCreateDTO.getIntakeYear());
        existingIntake.setBatch(intakeCreateDTO.getBatch());

        // Save the updated entity
        Intake updatedIntake = intakeRepository.save(existingIntake);

        // Map the updated entity to DTO and return
        IntakeDTO dto = modelMapper.map(updatedIntake, IntakeDTO.class);

        // Explicitly set entity IDs
        if (updatedIntake.getDepartment() != null) {
            dto.setDepartmentId(updatedIntake.getDepartment().getId());
        }

        return dto;
    }

    @Override
    public void deleteById(int theId) {
        intakeRepository.deleteById(theId);
    }
}