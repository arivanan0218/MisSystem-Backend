package com.ruh.mis.controller;


import com.ruh.mis.model.DTO.*;
import com.ruh.mis.model.Student;
import com.ruh.mis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/sintake/{departmentAndIntakeId}")
    public ResponseEntity<List<StudentDTO>> getStudents(@RequestParam int departmentId,
                                                              @RequestParam int intakeId) {
        List<StudentDTO> studentDTOS = studentService.getStudentByDepartmentIdAndIntakeId(departmentId, intakeId);
        return  ResponseEntity.ok(studentDTOS);
    }

    @PostMapping("/create")
    public StudentDTO addStudent(@RequestBody StudentCreateDTO theStudentCreateDTO) {
        Student savedStudent = studentService.save(theStudentCreateDTO);
        return studentService.findById(savedStudent.getId());
    }

    @PostMapping("/create-list")
    public ResponseEntity<String> addStudent(@RequestBody List<StudentCreateDTO> studentCreateDTOList) {
        studentService.saveStudentsList(studentCreateDTOList);
        return ResponseEntity.ok("Student List saved successfully");
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ROLE_AR')")
    @DeleteMapping("/{studentId}")
    public String deleteStudent(@PathVariable int studentId) {
        StudentDTO tempStudent = studentService.findById(studentId);

        if (tempStudent == null) {
            throw new RuntimeException("Student id not found: " + studentId);
        }

        studentService.deleteById(studentId);

        return "Deleted student id: " + studentId;
    }

    @PutMapping("/{studentId}") // New endpoint
    public StudentDTO updateStudent(@PathVariable int studentId, @RequestBody StudentCreateDTO studentCreateDTO) {
        return studentService.update(studentId, studentCreateDTO);
    }
}
