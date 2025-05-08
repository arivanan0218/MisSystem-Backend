package com.ruh.mis.service;

import com.ruh.mis.model.Student;
import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.DTO.StudentDTO;
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
    private UserService userService;  // Properly injected now

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
        // Save student info
        Student student = modelMapper.map(theStudentCreateDTO, Student.class);
        studentRepository.save(student);

        // Register student as a user
        userService.registerStudentUser(
                theStudentCreateDTO.getUsername(),
                theStudentCreateDTO.getEmail(),
                theStudentCreateDTO.getPassword()
        );

        return student;
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
        // Find the existing student
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        // Update the fields
        existingStudent.setName(studentCreateDTO.getName());
        existingStudent.setRegNo(studentCreateDTO.getRegNo());
        existingStudent.setEmail(studentCreateDTO.getEmail());
        existingStudent.setPhoneNumber(studentCreateDTO.getPhoneNumber());
        existingStudent.setNic(studentCreateDTO.getNic());
        existingStudent.setUsername(studentCreateDTO.getUsername());
        existingStudent.setPassword(studentCreateDTO.getPassword());

        // Save the updated entity
        Student updatedStudent = studentRepository.save(existingStudent);

        // Map the updated entity to DTO and return
        return modelMapper.map(updatedStudent, StudentDTO.class);
    }

    @Override
    public List<StudentDTO> getStudentByDepartmentIdAndIntakeId(int departmentId, int intakeId) {
        // Query students by departmentId and intakeId
        List<Student> students = studentRepository.findByDepartmentIdAndIntakeId(departmentId, intakeId);

        // Map each student entity to StudentDTO and return the list
        return students.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .collect(Collectors.toList());
    }

}
