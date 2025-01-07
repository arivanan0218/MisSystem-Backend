package com.ruh.mis.service;

import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.SemesterDTO;
import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.DTO.StudentDTO;
import com.ruh.mis.model.Student;

import java.util.List;

public interface StudentService {
    List<StudentDTO> findAll();

    StudentDTO findById(int theId);

    List<StudentDTO> getStudentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(int departmentId, int intakeId, int semesterId, int moduleId);

    Student save(StudentCreateDTO theStudentCreateDTO);

    void deleteById(int theId);

    StudentDTO update(int studentId, StudentCreateDTO studentCreateDTO);
}
