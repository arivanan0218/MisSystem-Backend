package com.ruh.mis.service;

import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Student;
import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.DTO.StudentDTO;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.IntakeRepository;
import com.ruh.mis.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private IntakeRepository intakeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    // Test data
    private Student testStudent;
    private Department testDepartment;
    private Intake testIntake;
    private StudentCreateDTO testStudentCreateDTO;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testDepartment = new Department();
        testDepartment.setId(1);
        testDepartment.setDepartmentName("Computer Science");

        testIntake = new Intake();
        testIntake.setId(1);
        testIntake.setIntakeYear("2022");
        testIntake.setBatch("A");

        testStudent = new Student();
        testStudent.setStudentRegNo("1");
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setStudentNIC("123456789V");
        testStudent.setStudentMail("john.doe@example.com");
        testStudent.setPhoneNumber("1234567890");
        testStudent.setUsername("johndoe");
        testStudent.setPassword("password123");
        testStudent.setGender("Male");
        testStudent.setDateOfBirth(LocalDate.of(2000, 1, 1));
        testStudent.setDepartment(testDepartment);
        testStudent.setIntake(testIntake);

        // Create DTO for testing
        testStudentCreateDTO = new StudentCreateDTO();
        testStudentCreateDTO.setStudentRegNo("2");
        testStudentCreateDTO.setFirstName("Jane");
        testStudentCreateDTO.setLastName("Smith");
        testStudentCreateDTO.setStudentNIC("987654321V");
        testStudentCreateDTO.setStudentMail("jane.smith@example.com");
        testStudentCreateDTO.setPhoneNumber("0987654321");
        testStudentCreateDTO.setUsername("janesmith");
        testStudentCreateDTO.setPassword("password456");
        testStudentCreateDTO.setGender("Female");
        testStudentCreateDTO.setDateOfBirth(LocalDate.of(2001, 2, 2));
        testStudentCreateDTO.setDepartmentId(1);
        testStudentCreateDTO.setIntakeId(1);

        // Create a DTO for response testing
        testStudentDTO = new StudentDTO();
        testStudentDTO.setStudentRegNo("1");
        testStudentDTO.setStudentName("John Doe"); // Combined name
        testStudentDTO.setStudentNIC("123456789V");
        testStudentDTO.setStudentMail("john.doe@example.com");
        testStudentDTO.setPhoneNumber("1234567890");
        testStudentDTO.setUsername("johndoe");
        testStudentDTO.setPassword("password123");
        testStudentDTO.setGender("Male");
        testStudentDTO.setDateOfBirth(LocalDate.of(2000, 1, 1));
        testStudentDTO.setDepartmentId(1);
        testStudentDTO.setIntakeId(1);
    }

    @Test
    void findAll_ShouldReturnAllStudents() {
        // Given
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);
        when(modelMapper.map(any(Student.class), eq(StudentDTO.class))).thenReturn(testStudentDTO);

        // When
        List<StudentDTO> result = studentService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getStudentName());
        assertEquals("123456789V", result.get(0).getStudentNIC());
        assertEquals("john.doe@example.com", result.get(0).getStudentMail());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals(1, result.get(0).getIntakeId());
        verify(studentRepository).findAll();
    }

    @Test
    void findById_WithValidId_ShouldReturnStudent() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(modelMapper.map(any(Student.class), eq(StudentDTO.class))).thenReturn(testStudentDTO);

        // When
        StudentDTO result = studentService.findById(1);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getStudentName());
        assertEquals("123456789V", result.getStudentNIC());
        assertEquals("john.doe@example.com", result.getStudentMail());
        assertEquals(1, result.getDepartmentId());
        assertEquals(1, result.getIntakeId());
        verify(studentRepository).findById(1);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        when(studentRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.findById(999);
        });

        assertTrue(exception.getMessage().contains("Student not found"));
        verify(studentRepository).findById(999);
    }

    @Test
    void save_ShouldSaveAndRegisterStudent() {
        // Given
        when(departmentRepository.findById(1)).thenReturn(Optional.of(testDepartment));
        when(intakeRepository.findById(1)).thenReturn(Optional.of(testIntake));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        doNothing().when(userService).registerStudentUser(anyString(), anyString(), anyString());

        // When
        Student result = studentService.save(testStudentCreateDTO);

        // Then
        assertNotNull(result);
        verify(departmentRepository).findById(1);
        verify(intakeRepository).findById(1);
        verify(studentRepository).save(any(Student.class));
        verify(userService).registerStudentUser(
                eq(testStudentCreateDTO.getUsername()),
                eq(testStudentCreateDTO.getStudentMail()),
                eq(testStudentCreateDTO.getPassword())
        );
    }

    @Test
    void save_WithInvalidDepartmentId_ShouldThrowException() {
        // Given
        when(departmentRepository.findById(999)).thenReturn(Optional.empty());
        testStudentCreateDTO.setDepartmentId(999);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.save(testStudentCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Department not found"));
        verify(departmentRepository).findById(999);
        verify(studentRepository, never()).save(any(Student.class));
        verify(userService, never()).registerStudentUser(anyString(), anyString(), anyString());
    }

    @Test
    void save_WithInvalidIntakeId_ShouldThrowException() {
        // Given
        when(departmentRepository.findById(1)).thenReturn(Optional.of(testDepartment));
        when(intakeRepository.findById(999)).thenReturn(Optional.empty());
        testStudentCreateDTO.setIntakeId(999);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.save(testStudentCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Intake not found"));
        verify(departmentRepository).findById(1);
        verify(intakeRepository).findById(999);
        verify(studentRepository, never()).save(any(Student.class));
        verify(userService, never()).registerStudentUser(anyString(), anyString(), anyString());
    }

    @Test
    void saveStudentsList_ShouldSaveAllStudents() {
        // Given
        List<StudentCreateDTO> dtoList = Arrays.asList(testStudentCreateDTO);
        when(modelMapper.map(any(StudentCreateDTO.class), eq(Student.class))).thenReturn(testStudent);
        when(studentRepository.saveAll(anyList())).thenReturn(Arrays.asList(testStudent));

        // When
        studentService.saveStudentsList(dtoList);

        // Then
        verify(modelMapper).map(any(StudentCreateDTO.class), eq(Student.class));
        verify(studentRepository).saveAll(anyList());
    }

    @Test
    void deleteById_ShouldDeleteStudent() {
        // Given
        doNothing().when(studentRepository).deleteById(1);

        // When
        studentService.deleteById(1);

        // Then
        verify(studentRepository).deleteById(1);
    }

    @Test
    void update_WithValidId_ShouldUpdateAndReturnStudent() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        when(modelMapper.map(any(Student.class), eq(StudentDTO.class))).thenReturn(testStudentDTO);

        testStudentCreateDTO.setFirstName("Updated");
        testStudentCreateDTO.setLastName("Name");
        testStudentCreateDTO.setStudentNIC("Updated-NIC");
        testStudentCreateDTO.setStudentMail("updated.email@example.com");

        // When
        StudentDTO result = studentService.update(1, testStudentCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated", testStudent.getFirstName());
        assertEquals("Name", testStudent.getLastName());
        assertEquals("Updated-NIC", testStudent.getStudentNIC());
        assertEquals("updated.email@example.com", testStudent.getStudentMail());
        verify(studentRepository).findById(1);
        verify(studentRepository).save(testStudent);
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Given
        when(studentRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.update(999, testStudentCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Student not found"));
        verify(studentRepository).findById(999);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void getStudentByDepartmentIdAndIntakeId_ShouldReturnMatchingStudents() {
        // Given
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findByDepartmentIdAndIntakeId(1, 1)).thenReturn(students);
        when(modelMapper.map(any(Student.class), eq(StudentDTO.class))).thenReturn(testStudentDTO);

        // When
        List<StudentDTO> result = studentService.getStudentByDepartmentIdAndIntakeId(1, 1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getStudentName());
        assertEquals("123456789V", result.get(0).getStudentNIC());
        assertEquals("john.doe@example.com", result.get(0).getStudentMail());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals(1, result.get(0).getIntakeId());
        verify(studentRepository).findByDepartmentIdAndIntakeId(1, 1);
    }
}