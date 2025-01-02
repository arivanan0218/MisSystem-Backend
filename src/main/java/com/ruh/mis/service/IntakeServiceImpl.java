package com.ruh.mis.service;

import com.ruh.mis.model.DTO.DepartmentCreateDTO;
import com.ruh.mis.model.DTO.DepartmentDTO;
import com.ruh.mis.model.DTO.IntakeCreateDTO;
import com.ruh.mis.model.DTO.IntakeDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.repository.IntakeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IntakeServiceImpl implements IntakeService{

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<IntakeDTO> findAll() {
        return intakeRepository.findAll().stream()
                .map(intake -> modelMapper.map(intake, IntakeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public IntakeDTO findById(int theId) {
        Intake intake = intakeRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Intake not found: " + theId));
        return modelMapper.map(intake, IntakeDTO.class);
    }

    @Override
    public List<IntakeDTO> getIntakesByDepartmentId(int departmentId) {
        List<Intake> intakes = intakeRepository.findByDepartmentId(departmentId);

        // Use ModelMapper to map List<Intake> to List<IntakeDTO>
        return intakes.stream()
                .map(intake -> modelMapper.map(intake, IntakeDTO.class))  // Mapping using ModelMapper
                .collect(Collectors.toList());
    }

    @Override
    public Intake save(IntakeCreateDTO theIntakeCreateDTO) {
        Intake intake = modelMapper.map(theIntakeCreateDTO, Intake.class);
        Intake savedIntake = intakeRepository.save(intake);
        return savedIntake;
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
        return modelMapper.map(updatedIntake, IntakeDTO.class);
    }

    @Override
    public void deleteById(int theId) {
        intakeRepository.deleteById(theId);
    }
}
