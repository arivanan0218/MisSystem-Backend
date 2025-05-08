package com.ruh.mis.service;

import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Student;
import com.ruh.mis.model.Assignment;
// Use the fully qualified name for Module to avoid ambiguity
// import com.ruh.mis.model.Module; - Removed this import
import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.repository.AssignmentRepository;
import com.ruh.mis.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceImplTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    // Test data
    private Assignment testAssignment;
    private Student testStudent;
    private Department testDepartment;
    private Intake testIntake;
    private Semester testSemester;
    private com.ruh.mis.model.Module testModule; // Using fully qualified name
    private AssignmentCreateDTO testAssignmentCreateDTO;
    private AssignmentDTO testAssignmentDTO;
    private MarksCreateDTO testMarksCreateDTO;
    private MarksDTO testMarksDTO;

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

        testSemester = new Semester();
        testSemester.setId(1);
        testSemester.setSemesterName("Semester 1");

        testModule = new com.ruh.mis.model.Module(); // Using fully qualified name
        testModule.setId(1);
        testModule.setModuleName("Programming Fundamentals");

        testStudent = new Student();
        testStudent.setStudentRegNo("12345");
        testStudent.setStudentName("John Doe");

        testAssignment = new Assignment();
        testAssignment.setId(1);
        testAssignment.setAssignmentName("Assignment 1");
        testAssignment.setAssignmentPercentage(20);
        testAssignment.setDepartment(testDepartment);
        testAssignment.setIntake(testIntake);
        testAssignment.setSemester(testSemester);
        testAssignment.setModule(testModule);
        testAssignment.setStudents(List.of(testStudent));

        // Create DTOs for testing
        testAssignmentCreateDTO = new AssignmentCreateDTO();
        testAssignmentCreateDTO.setAssignmentName("New Assignment");
        testAssignmentCreateDTO.setAssignmentPercentage(25);
        testAssignmentCreateDTO.setDepartmentId(1);
        testAssignmentCreateDTO.setIntakeId(1);
        testAssignmentCreateDTO.setSemesterId(1);
        testAssignmentCreateDTO.setModuleId(1);

        testAssignmentDTO = new AssignmentDTO();
        testAssignmentDTO.setId(1);
        testAssignmentDTO.setAssignmentName("Assignment 1");
        testAssignmentDTO.setAssignmentPercentage(20);
        testAssignmentDTO.setDepartmentId(1);
        testAssignmentDTO.setIntakeId(1);
        testAssignmentDTO.setSemesterId(1);
        testAssignmentDTO.setModuleId(1);

        // Instead of trying to set properties directly, we'll handle this in the mock setup
        testMarksCreateDTO = new MarksCreateDTO();
        // We'll assume the StudentId is a String type based on errors
        testMarksCreateDTO.setStudentId(12345);

        testMarksDTO = new MarksDTO();
        testMarksDTO.setId(1);
        testMarksDTO.setAssignmentName("Assignment 1");
        testMarksDTO.setStudentId(12345);
        testMarksDTO.setStudent_name("John Doe");
    }

    @Test
    void findAll_ShouldReturnAllAssignments() {
        // Given
        List<Assignment> assignments = Arrays.asList(testAssignment);
        when(assignmentRepository.findAll()).thenReturn(assignments);
        when(modelMapper.map(any(Assignment.class), eq(AssignmentDTO.class))).thenReturn(testAssignmentDTO);

        // When
        List<AssignmentDTO> result = assignmentService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Assignment 1", result.get(0).getAssignmentName());
        assertEquals(20, result.get(0).getAssignmentPercentage());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals(1, result.get(0).getIntakeId());
        assertEquals(1, result.get(0).getSemesterId());
        assertEquals(1, result.get(0).getModuleId());
        verify(assignmentRepository).findAll();
    }

    @Test
    void findAllMarks_ShouldReturnAllMarks() {
        // Given
        List<Assignment> assignments = Arrays.asList(testAssignment);
        when(assignmentRepository.findAll()).thenReturn(assignments);
        when(modelMapper.map(any(Assignment.class), eq(MarksDTO.class))).thenReturn(testMarksDTO);

        // When
        List<MarksDTO> result = assignmentService.findAllMarks();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Assignment 1", result.get(0).getAssignmentName());
        assertEquals(12345, result.get(0).getStudentId());
        assertEquals("John Doe", result.get(0).getStudent_name());
        verify(assignmentRepository).findAll();
    }

    @Test
    void findById_WithValidId_ShouldReturnAssignment() {
        // Given
        when(assignmentRepository.findById(1)).thenReturn(Optional.of(testAssignment));
        when(modelMapper.map(any(Assignment.class), eq(AssignmentDTO.class))).thenReturn(testAssignmentDTO);

        // When
        AssignmentDTO result = assignmentService.findById(1);

        // Then
        assertNotNull(result);
        assertEquals("Assignment 1", result.getAssignmentName());
        assertEquals(20, result.getAssignmentPercentage());
        assertEquals(1, result.getDepartmentId());
        assertEquals(1, result.getIntakeId());
        assertEquals(1, result.getSemesterId());
        assertEquals(1, result.getModuleId());
        verify(assignmentRepository).findById(1);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        when(assignmentRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            assignmentService.findById(999);
        });

        assertTrue(exception.getMessage().contains("Assignment not found"));
        verify(assignmentRepository).findById(999);
    }

    @Test
    void getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId_ShouldReturnMatchingAssignments() {
        // Given
        List<Assignment> assignments = Arrays.asList(testAssignment);
        when(assignmentRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(1, 1, 1, 1))
                .thenReturn(assignments);
        when(modelMapper.map(any(Assignment.class), eq(AssignmentDTO.class))).thenReturn(testAssignmentDTO);

        // When
        List<AssignmentDTO> result = assignmentService.getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(1, 1, 1, 1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Assignment 1", result.get(0).getAssignmentName());
        assertEquals(20, result.get(0).getAssignmentPercentage());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals(1, result.get(0).getIntakeId());
        assertEquals(1, result.get(0).getSemesterId());
        assertEquals(1, result.get(0).getModuleId());
        verify(assignmentRepository).findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(1, 1, 1, 1);
    }

    @Test
    void save_ShouldSaveAndReturnAssignment() {
        // Given
        when(modelMapper.map(any(AssignmentCreateDTO.class), eq(Assignment.class))).thenReturn(testAssignment);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(testAssignment);

        // When
        Assignment result = assignmentService.save(testAssignmentCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Assignment 1", result.getAssignmentName());
        assertEquals(20, result.getAssignmentPercentage());
        verify(modelMapper).map(eq(testAssignmentCreateDTO), eq(Assignment.class));
        verify(assignmentRepository).save(any(Assignment.class));
    }

    @Test
    void saveMarksList_WithValidData_ShouldSaveMarks() {
        // Given
        when(modelMapper.map(any(MarksCreateDTO.class), eq(Assignment.class))).thenReturn(testAssignment);
        when(studentRepository.findById(12345)).thenReturn(Optional.of(testStudent));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(testAssignment);

        // When
        List<MarksCreateDTO> marksList = Arrays.asList(testMarksCreateDTO);
        assignmentService.saveMarksList(marksList);

        // Then
        verify(modelMapper).map(eq(testMarksCreateDTO), eq(Assignment.class));
        verify(studentRepository).findById(12345);
        verify(assignmentRepository).save(any(Assignment.class));
    }

    @Test
    void saveMarksList_WithInvalidStudentId_ShouldThrowException() {
        // Given
        when(modelMapper.map(any(MarksCreateDTO.class), eq(Assignment.class))).thenReturn(testAssignment);
        when(studentRepository.findById(99999)).thenReturn(Optional.empty());

        // Create a new MarksCreateDTO instance for this test to avoid affecting other tests
        MarksCreateDTO invalidMarksDTO = new MarksCreateDTO();
        invalidMarksDTO.setStudentId(99999);

        // When & Then
        List<MarksCreateDTO> marksList = Arrays.asList(invalidMarksDTO);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            assignmentService.saveMarksList(marksList);
        });

        assertTrue(exception.getMessage().contains("Invalid student ID"));
        verify(modelMapper).map(eq(invalidMarksDTO), eq(Assignment.class));
        verify(studentRepository).findById(99999);
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }

    @Test
    void deleteById_ShouldDeleteAssignment() {
        // Given
        doNothing().when(assignmentRepository).deleteById(1);

        // When
        assignmentService.deleteById(1);

        // Then
        verify(assignmentRepository).deleteById(1);
    }

    @Test
    void update_WithValidId_ShouldUpdateAndReturnAssignment() {
        // Given
        when(assignmentRepository.findById(1)).thenReturn(Optional.of(testAssignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(testAssignment);
        when(modelMapper.map(any(Assignment.class), eq(AssignmentDTO.class))).thenReturn(testAssignmentDTO);

        testAssignmentCreateDTO.setAssignmentName("Updated Assignment");
        testAssignmentCreateDTO.setAssignmentPercentage(30);

        // When
        AssignmentDTO result = assignmentService.update(1, testAssignmentCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated Assignment", testAssignment.getAssignmentName());
        assertEquals(30, testAssignment.getAssignmentPercentage());
        verify(assignmentRepository).findById(1);
        verify(assignmentRepository).save(testAssignment);
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Given
        when(assignmentRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            assignmentService.update(999, testAssignmentCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Assignment not found"));
        verify(assignmentRepository).findById(999);
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }
}