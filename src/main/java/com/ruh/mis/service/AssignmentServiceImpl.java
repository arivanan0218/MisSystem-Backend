package com.ruh.mis.service;

import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.Assignment;
import com.ruh.mis.repository.AssignmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AssignmentDTO> findAll() {
        return assignmentRepository.findAll().stream()
                .map(assignment -> modelMapper.map(assignment, AssignmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentDTO findById(int theId) {
        Assignment assignment = assignmentRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Assignment not found: " + theId));
        return modelMapper.map(assignment, AssignmentDTO.class);
    }

    @Override
    public Assignment save(AssignmentCreateDTO theAssignmentCreateDTO) {
        Assignment assignment = modelMapper.map(theAssignmentCreateDTO, Assignment.class);
        return assignmentRepository.save(assignment);
    }

    @Override
    public void deleteById(int theId) {
        assignmentRepository.deleteById(theId);
    }
}
