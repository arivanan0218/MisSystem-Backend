package com.ruh.mis.service;

import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.LecturerDTO;
import com.ruh.mis.model.Lecturer;
import com.ruh.mis.repository.LecturerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<LecturerDTO> findAll() {
        return lecturerRepository.findAll().stream()
                .map(lecturer -> modelMapper.map(lecturer, LecturerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public LecturerDTO findById(int theId) {
        Lecturer lecturer = lecturerRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found: " + theId));
        return modelMapper.map(lecturer, LecturerDTO.class);
    }

    @Override
    public Lecturer save(LecturerCreateDTO theLecturerCreateDTO) {
        Lecturer lecturer = modelMapper.map(theLecturerCreateDTO, Lecturer.class);
        return lecturerRepository.save(lecturer);
    }

    @Override
    public void deleteById(int theId) {
        lecturerRepository.deleteById(theId);
    }
}
