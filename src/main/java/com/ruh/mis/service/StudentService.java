package com.ruh.mis.service;

import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.DTO.StudentDTO;
import com.ruh.mis.model.Student;

import java.util.List;

public interface StudentService {
    List<StudentDTO> findAll();

    StudentDTO findById(int theId);

    Student save(StudentCreateDTO theStudentCreateDTO);

    void deleteById(int theId);
}
