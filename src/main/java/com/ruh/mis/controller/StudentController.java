package com.ruh.mis.controller;


import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.DTO.StudentDTO;
import com.ruh.mis.model.Student;
import com.ruh.mis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public List<StudentDTO> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/{studentId}")
    public StudentDTO getStudent(@PathVariable int studentId) {
        StudentDTO theStudent = studentService.findById(studentId);

        if (theStudent == null) {
            throw new RuntimeException("Student id not found: " + studentId);
        }

        return theStudent;
    }

    @PostMapping("/create")
    public StudentDTO addStudent(@RequestBody StudentCreateDTO theStudentCreateDTO) {
        Student savedStudent = studentService.save(theStudentCreateDTO);
        return studentService.findById(savedStudent.getId());
    }

    @DeleteMapping("/{studentId}")
    public String deleteStudent(@PathVariable int studentId) {
        StudentDTO tempStudent = studentService.findById(studentId);

        if (tempStudent == null) {
            throw new RuntimeException("Student id not found: " + studentId);
        }

        studentService.deleteById(studentId);

        return "Deleted student id: " + studentId;
    }
}
