package com.ruh.mis.service;

import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.DTO.StudentDTO;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<StudentDTO> findAll() {
        return studentRepository.findAll().stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO findById(int theId) {
        Student student = studentRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + theId));
        return modelMapper.map(student, StudentDTO.class);
    }

    @Override
    public Student save(StudentCreateDTO theStudentCreateDTO) {
        Student student = modelMapper.map(theStudentCreateDTO, Student.class);
        return studentRepository.save(student);
    }

    @Override
    public void deleteById(int theId) {
        studentRepository.deleteById(theId);
    }
}
