package com.ruh.mis.service;

import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.SemesterDTO;
import com.ruh.mis.model.Semester;
import com.ruh.mis.repository.SemesterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SemesterDTO> findAll() {
        return semesterRepository.findAll().stream()
                .map(semester -> modelMapper.map(semester, SemesterDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SemesterDTO findById(int theId) {
        Semester semester = semesterRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Semester not found: " + theId));
        return modelMapper.map(semester, SemesterDTO.class);
    }

    @Override
    public Semester save(SemesterCreateDTO theSemesterCreateDTO) {
        Semester semester = modelMapper.map(theSemesterCreateDTO, Semester.class);
        return semesterRepository.save(semester);
    }

    @Override
    public void deleteById(int theId) {
        semesterRepository.deleteById(theId);
    }
}
