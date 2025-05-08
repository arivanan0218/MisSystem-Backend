package com.ruh.mis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.DTO.StudentDTO;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.StudentRepository;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.IntakeRepository;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private UserService userService;  // Properly injected now

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private ModelMapper modelMapper;
    


    @Override
    public List<StudentDTO> findAll() {
        return studentRepository.findAll().stream()
                .map(student -> {
                    // Map student entity to DTO
                    StudentDTO dto = modelMapper.map(student, StudentDTO.class);
                    
                    // Explicitly set entity IDs
                    if (student.getDepartment() != null) {
                        dto.setDepartmentId(student.getDepartment().getId());
                    }
                    
                    if (student.getIntake() != null) {
                        dto.setIntakeId(student.getIntake().getId());
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO findById(int theId) {
        Student student = studentRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + theId));
        
        // Map student entity to DTO
        StudentDTO dto = modelMapper.map(student, StudentDTO.class);
        
        // Explicitly set entity IDs
        if (student.getDepartment() != null) {
            dto.setDepartmentId(student.getDepartment().getId());
        }
        
        if (student.getIntake() != null) {
            dto.setIntakeId(student.getIntake().getId());
        }
        
        return dto;
    }

    @Override
    public Student save(StudentCreateDTO theStudentCreateDTO) {
        // Create a new student entity and set firstName and lastName directly
        Student student = new Student();
        
        // Set firstName and lastName directly
        student.setFirstName(theStudentCreateDTO.getFirstName());
        student.setLastName(theStudentCreateDTO.getLastName());
        // studentName will be automatically generated via the getter
        
        // Map other properties from DTO to entity
        student.setStudentRegNo(theStudentCreateDTO.getStudentRegNo());
        student.setStudentNIC(theStudentCreateDTO.getStudentNIC());
        student.setStudentMail(theStudentCreateDTO.getStudentMail());
        student.setPhoneNumber(theStudentCreateDTO.getPhoneNumber());
        student.setUsername(theStudentCreateDTO.getUsername());
        student.setPassword(theStudentCreateDTO.getPassword());
        student.setGender(theStudentCreateDTO.getGender());
        student.setDateOfBirth(theStudentCreateDTO.getDateOfBirth());
        
        // Set department and intake
        student.setDepartment(departmentRepository.findById(theStudentCreateDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found: " + theStudentCreateDTO.getDepartmentId())));
        student.setIntake(intakeRepository.findById(theStudentCreateDTO.getIntakeId())
                .orElseThrow(() -> new RuntimeException("Intake not found: " + theStudentCreateDTO.getIntakeId())));
        
        studentRepository.save(student);

        // Register student as a user
        userService.registerStudentUser(
                theStudentCreateDTO.getUsername(),
                theStudentCreateDTO.getStudentMail(),
                theStudentCreateDTO.getPassword()
        );

        return student;
    }

    @Override
    @Transactional
    public void saveStudentsList(List<StudentCreateDTO> studentCreateDTOList) {
        for (StudentCreateDTO dto : studentCreateDTOList) {
            try {
                // Create a new student entity
                Student student = new Student();
                
                // Set basic properties
                student.setFirstName(dto.getFirstName());
                student.setLastName(dto.getLastName());
                student.setStudentRegNo(dto.getStudentRegNo());
                student.setStudentNIC(dto.getStudentNIC());
                student.setStudentMail(dto.getStudentMail());
                student.setPhoneNumber(dto.getPhoneNumber());
                student.setUsername(dto.getUsername());
                student.setPassword(dto.getPassword());
                student.setGender(dto.getGender());
                student.setDateOfBirth(dto.getDateOfBirth());
                
                // Set department and intake by fetching the actual entities
                student.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found: " + dto.getDepartmentId())));
                student.setIntake(intakeRepository.findById(dto.getIntakeId())
                        .orElseThrow(() -> new RuntimeException("Intake not found: " + dto.getIntakeId())));
                
                // Save the student
                studentRepository.save(student);
                
                // Register student as a user
                userService.registerStudentUser(
                        dto.getUsername(),
                        dto.getStudentMail(),
                        dto.getPassword()
                );
            } catch (Exception e) {
                // Log the error and continue with the next student
                System.err.println("Error saving student: " + e.getMessage());
                // You might want to collect errors and return them to the client
            }
        }
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

        // Update firstName and lastName directly
        if (studentCreateDTO.getFirstName() != null) {
            existingStudent.setFirstName(studentCreateDTO.getFirstName());
        }
        
        if (studentCreateDTO.getLastName() != null) {
            existingStudent.setLastName(studentCreateDTO.getLastName());
        }
        // studentName will be automatically generated via the getter
        
        existingStudent.setStudentRegNo(studentCreateDTO.getStudentRegNo());
        existingStudent.setStudentMail(studentCreateDTO.getStudentMail());
        existingStudent.setPhoneNumber(studentCreateDTO.getPhoneNumber());
        existingStudent.setStudentNIC(studentCreateDTO.getStudentNIC());
        existingStudent.setUsername(studentCreateDTO.getUsername());
        existingStudent.setPassword(studentCreateDTO.getPassword());
        
        // Set new fields
        if (studentCreateDTO.getGender() != null) {
            existingStudent.setGender(studentCreateDTO.getGender());
        }
        
        if (studentCreateDTO.getDateOfBirth() != null) {
            existingStudent.setDateOfBirth(studentCreateDTO.getDateOfBirth());
        }

        // Save the updated entity
        Student updatedStudent = studentRepository.save(existingStudent);

        // Map the updated entity to DTO with proper ID handling and return
        StudentDTO dto = modelMapper.map(updatedStudent, StudentDTO.class);
        
        // Explicitly set entity IDs
        if (updatedStudent.getDepartment() != null) {
            dto.setDepartmentId(updatedStudent.getDepartment().getId());
        }
        
        if (updatedStudent.getIntake() != null) {
            dto.setIntakeId(updatedStudent.getIntake().getId());
        }
        
        return dto;
    }

    @Override
    public List<StudentDTO> getStudentByDepartmentIdAndIntakeId(int departmentId, int intakeId) {
        // Query students by departmentId and intakeId
        List<Student> students = studentRepository.findByDepartmentIdAndIntakeId(departmentId, intakeId);

        // Map each student entity to StudentDTO with proper ID handling and return the list
        return students.stream()
                .map(student -> {
                    StudentDTO dto = modelMapper.map(student, StudentDTO.class);
                    
                    // Explicitly set entity IDs
                    if (student.getDepartment() != null) {
                        dto.setDepartmentId(student.getDepartment().getId());
                    }
                    
                    if (student.getIntake() != null) {
                        dto.setIntakeId(student.getIntake().getId());
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
