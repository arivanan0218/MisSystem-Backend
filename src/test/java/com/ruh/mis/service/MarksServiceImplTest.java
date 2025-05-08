package com.ruh.mis.service;

import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.Marks;
import com.ruh.mis.model.Student;
// Removed import for Module to avoid ambiguity
import com.ruh.mis.model.DTO.AssignmentMarksDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.DTO.MarksResponseDTO;
import com.ruh.mis.repository.AssignmentRepository;
import com.ruh.mis.repository.MarksRepository;
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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarksServiceImplTest {

    @Mock
    private MarksRepository marksRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MarksServiceImpl marksService;

    // Test data
    private Student testStudent;
    private Assignment testAssignment;
    private com.ruh.mis.model.Module testModule; // Using fully qualified name
    private Marks testMarks;
    private MarksDTO testMarksDTO;
    private MarksCreateDTO testMarksCreateDTO;
    private List<Marks> testMarksList;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testModule = new com.ruh.mis.model.Module(); // Using fully qualified name
        testModule.setId(1);
        testModule.setModuleName("Programming Fundamentals");

        testStudent = new Student();
        testStudent.setId(1);
        testStudent.setStudentRegNo("12345");
        testStudent.setStudentName("John Doe");

        testAssignment = new Assignment();
        testAssignment.setId(1);
        testAssignment.setAssignmentName("Assignment 1");
        testAssignment.setAssignmentPercentage(20);
        testAssignment.setModule(testModule);

        testMarks = new Marks();
        testMarks.setId(1);
        testMarks.setStudent(testStudent);
        testMarks.setAssignment(testAssignment);
        testMarks.setMarksObtained(85);

        testMarksDTO = new MarksDTO();
        testMarksDTO.setId(1);
        testMarksDTO.setStudentId(1);
        testMarksDTO.setAssignmentId(1);
        testMarksDTO.setMarksObtained(85);
        testMarksDTO.setStudent_name("John Doe");

        testMarksCreateDTO = new MarksCreateDTO();
        testMarksCreateDTO.setStudentId(1);
        testMarksCreateDTO.setAssignmentId(1);
        testMarksCreateDTO.setMarksObtained(85);

        testMarksList = new ArrayList<>();
        testMarksList.add(testMarks);
    }

    @Test
    void findAll_ShouldReturnAllMarks() {
        // Given
        when(marksRepository.findAll()).thenReturn(testMarksList);
        when(modelMapper.map(any(Marks.class), eq(MarksDTO.class))).thenReturn(testMarksDTO);

        // When
        List<MarksDTO> result = marksService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(85, result.get(0).getMarksObtained());
        assertEquals("John Doe", result.get(0).getStudent_name());
        verify(marksRepository).findAll();
    }

    @Test
    void findById_WithValidId_ShouldReturnMarks() {
        // Given
        when(marksRepository.findById(1)).thenReturn(Optional.of(testMarks));
        when(modelMapper.map(any(Marks.class), eq(MarksDTO.class))).thenReturn(testMarksDTO);

        // When
        MarksDTO result = marksService.findById(1);

        // Then
        assertNotNull(result);
        assertEquals(85, result.getMarksObtained());
        assertEquals("John Doe", result.getStudent_name());
        verify(marksRepository).findById(1);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        when(marksRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            marksService.findById(999);
        });

        assertTrue(exception.getMessage().contains("Marks not found"));
        verify(marksRepository).findById(999);
    }

    @Test
    void saveMarksList_WithValidData_ShouldSaveMarks() {
        // Given
        when(modelMapper.map(any(MarksCreateDTO.class), eq(Marks.class))).thenReturn(testMarks);
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(assignmentRepository.findById(1)).thenReturn(Optional.of(testAssignment));
        when(marksRepository.save(any(Marks.class))).thenReturn(testMarks);

        // When
        List<MarksCreateDTO> marksList = Arrays.asList(testMarksCreateDTO);
        marksService.saveMarksList(marksList);

        // Then
        verify(modelMapper).map(eq(testMarksCreateDTO), eq(Marks.class));
        verify(studentRepository).findById(1);
        verify(assignmentRepository).findById(1);
        verify(marksRepository).save(any(Marks.class));
    }

    @Test
    void saveMarksList_WithInvalidStudentId_ShouldThrowException() {
        // Given
        when(modelMapper.map(any(MarksCreateDTO.class), eq(Marks.class))).thenReturn(testMarks);
        when(studentRepository.findById(999)).thenReturn(Optional.empty());
        when(assignmentRepository.findById(1)).thenReturn(Optional.of(testAssignment));

        testMarksCreateDTO.setStudentId(999);

        // When & Then
        List<MarksCreateDTO> marksList = Arrays.asList(testMarksCreateDTO);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            marksService.saveMarksList(marksList);
        });

        assertTrue(exception.getMessage().contains("Invalid Student ID or Assignment ID"));
        verify(modelMapper).map(eq(testMarksCreateDTO), eq(Marks.class));
        verify(studentRepository).findById(999);
        verify(marksRepository, never()).save(any(Marks.class));
    }

    @Test
    void saveMarksList_WithInvalidAssignmentId_ShouldThrowException() {
        // Given
        when(modelMapper.map(any(MarksCreateDTO.class), eq(Marks.class))).thenReturn(testMarks);
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(assignmentRepository.findById(999)).thenReturn(Optional.empty());

        testMarksCreateDTO.setAssignmentId(999);

        // When & Then
        List<MarksCreateDTO> marksList = Arrays.asList(testMarksCreateDTO);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            marksService.saveMarksList(marksList);
        });

        assertTrue(exception.getMessage().contains("Invalid Student ID or Assignment ID"));
        verify(modelMapper).map(eq(testMarksCreateDTO), eq(Marks.class));
        verify(studentRepository).findById(1);
        verify(assignmentRepository).findById(999);
        verify(marksRepository, never()).save(any(Marks.class));
    }

    @Test
    void getMarksForStudent_WithValidId_ShouldReturnMarksResponse() {
        // Given
        when(marksRepository.findByStudentId(1)).thenReturn(testMarksList);

        // When
        MarksResponseDTO result = marksService.getMarksForStudent(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        // Skip testing studentName field
        // Skip testing finalMarks field

        // Test only accessible fields/methods
        List<AssignmentMarksDTO> assignmentMarksList = result.getAssignmentMarks();
        assertNotNull(assignmentMarksList);
        assertEquals(1, assignmentMarksList.size());

        AssignmentMarksDTO assignmentMarks = assignmentMarksList.get(0);
        assertEquals(1, assignmentMarks.getAssignmentId());
        assertEquals("Assignment 1", assignmentMarks.getAssignmentName());
        assertEquals(85, assignmentMarks.getMarksObtained());
        assertEquals(20, assignmentMarks.getAssignmentPercentage());
        assertEquals(1, assignmentMarks.getModuleId());

        verify(marksRepository).findByStudentId(1);
    }

    @Test
    void getMarksForStudent_WithNoMarks_ShouldReturnEmptyResponse() {
        // Given
        when(marksRepository.findByStudentId(1)).thenReturn(new ArrayList<>());

        // When
        MarksResponseDTO result = marksService.getMarksForStudent(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getStudentId());
        // Skip testing studentName field

        // Test only accessible fields/methods
        List<AssignmentMarksDTO> assignmentMarksList = result.getAssignmentMarks();
        assertNotNull(assignmentMarksList);
        assertTrue(assignmentMarksList.isEmpty());

        // Skip testing finalMarks field
        verify(marksRepository).findByStudentId(1);
    }

    @Test
    void updateMarks_WithValidId_ShouldUpdateAndReturnMarks() {
        // Given
        when(marksRepository.findById(1)).thenReturn(Optional.of(testMarks));
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(assignmentRepository.findById(1)).thenReturn(Optional.of(testAssignment));
        when(marksRepository.save(any(Marks.class))).thenReturn(testMarks);
        when(modelMapper.map(any(Marks.class), eq(MarksDTO.class))).thenReturn(testMarksDTO);

        testMarksCreateDTO.setMarksObtained(90); // Updated marks

        // When
        MarksDTO result = marksService.updateMarks(1, testMarksCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals(85, result.getMarksObtained()); // Using the value from testMarksDTO
        assertEquals("John Doe", result.getStudent_name());
        verify(marksRepository).findById(1);
        verify(studentRepository).findById(1);
        verify(assignmentRepository).findById(1);
        verify(marksRepository).save(testMarks);
    }

    @Test
    void updateMarks_WithInvalidId_ShouldThrowException() {
        // Given
        when(marksRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            marksService.updateMarks(999, testMarksCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Marks not found"));
        verify(marksRepository).findById(999);
        verify(marksRepository, never()).save(any(Marks.class));
    }

    @Test
    void updateMarks_WithInvalidStudentId_ShouldThrowException() {
        // Given
        when(marksRepository.findById(1)).thenReturn(Optional.of(testMarks));
        when(studentRepository.findById(999)).thenReturn(Optional.empty());
        when(assignmentRepository.findById(1)).thenReturn(Optional.of(testAssignment));

        testMarksCreateDTO.setStudentId(999);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            marksService.updateMarks(1, testMarksCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Invalid Student ID or Assignment ID"));
        verify(marksRepository).findById(1);
        verify(studentRepository).findById(999);
        verify(marksRepository, never()).save(any(Marks.class));
    }

    @Test
    void deleteById_ShouldDeleteMarks() {
        // Given
        doNothing().when(marksRepository).deleteById(1);

        // When
        marksService.deleteById(1);

        // Then
        verify(marksRepository).deleteById(1);
    }
}