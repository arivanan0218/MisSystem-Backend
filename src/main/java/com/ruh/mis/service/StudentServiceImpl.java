package com.ruh.mis.service;

import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.DTO.*;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<StudentDTO> getStudentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(int departmentId, int intakeId, int semesterId, int moduleId) {
        List<Student> students = studentRepository.findStudentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(departmentId, intakeId, semesterId, moduleId);

        return students.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Student save(StudentCreateDTO theStudentCreateDTO) {
        Student student = modelMapper.map(theStudentCreateDTO, Student.class);
        return studentRepository.save(student);
    }

    @Override
    public void saveStudentsList(List<StudentCreateDTO> studentCreateDTOList) {
        List<Student> studentList = studentCreateDTOList.stream()
                .map(dto -> {
                    Student student = modelMapper.map(dto, Student.class);

                    return student;
                })
                .collect(Collectors.toList());

        studentRepository.saveAll(studentList);
    }

    @Override
    public void deleteById(int theId) {
        studentRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public StudentDTO update(int studentId, StudentCreateDTO studentCreateDTO) {
        // Find the existing department
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        // Update the fields
        existingStudent.setStudent_name(studentCreateDTO.getStudent_name());
        existingStudent.setStudent_Reg_No(studentCreateDTO.getStudent_Reg_No());
        existingStudent.setStudent_email(studentCreateDTO.getStudent_email());
        existingStudent.setStudent_NIC(studentCreateDTO.getStudent_NIC());


        // Save the updated entity
        Student updatedStudent = studentRepository.save(existingStudent);

        // Map the updated entity to DTO and return
        return modelMapper.map(updatedStudent, StudentDTO.class);
    }
}
