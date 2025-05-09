package com.ruh.mis.service;

import com.ruh.mis.model.Department;
import com.ruh.mis.model.Lecturer;
import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.LecturerDTO;
import com.ruh.mis.repository.LecturerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LecturerServiceImplTest {

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private LecturerServiceImpl lecturerService;

    // Test data
    private Lecturer testLecturer;
    private Department testDepartment;
    private LecturerCreateDTO testLecturerCreateDTO;
    private LecturerDTO testLecturerDTO;

    @BeforeEach
    void setUp() {
        // Initialize department
        testDepartment = new Department();
        testDepartment.setId(1);
        testDepartment.setDepartmentName("Computer Science");

        // Initialize lecturer
        testLecturer = new Lecturer();
        testLecturer.setId(1);
        testLecturer.setLecturerEmail("john.smith@example.com");
        testLecturer.setUsername("johnsmith");
        testLecturer.setPassword("password123");
        testLecturer.setDepartment(testDepartment);
        // Set any other fields your Lecturer class actually has

        // Initialize LecturerCreateDTO
        testLecturerCreateDTO = new LecturerCreateDTO();
        testLecturerCreateDTO.setLecturerEmail("jane.doe@example.com");
        testLecturerCreateDTO.setUsername("janedoe");
        testLecturerCreateDTO.setPassword("password456");
        testLecturerCreateDTO.setDepartmentId(1);
        // Set any other fields your LecturerCreateDTO class actually has

        // Initialize LecturerDTO
        testLecturerDTO = new LecturerDTO();
        testLecturerDTO.setId(1);
        testLecturerDTO.setLecturerEmail("john.smith@example.com");
        testLecturerDTO.setUsername("johnsmith");
        testLecturerDTO.setPassword("password123");
        testLecturerDTO.setDepartmentId(1);
        // Set any other fields your LecturerDTO class actually has
    }

    @Test
    void findAll_ShouldReturnAllLecturers() {
        // Given
        List<Lecturer> lecturers = Arrays.asList(testLecturer);
        when(lecturerRepository.findAll()).thenReturn(lecturers);
        when(modelMapper.map(any(Lecturer.class), eq(LecturerDTO.class))).thenReturn(testLecturerDTO);

        // When
        List<LecturerDTO> result = lecturerService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("john.smith@example.com", result.get(0).getLecturerEmail());
        assertEquals("johnsmith", result.get(0).getUsername());
        assertEquals(1, result.get(0).getDepartmentId());
        verify(lecturerRepository).findAll();
    }

    @Test
    void findById_WithValidId_ShouldReturnLecturer() {
        // Given
        when(lecturerRepository.findById(1)).thenReturn(Optional.of(testLecturer));
        when(modelMapper.map(any(Lecturer.class), eq(LecturerDTO.class))).thenReturn(testLecturerDTO);

        // When
        LecturerDTO result = lecturerService.findById(1);

        // Then
        assertNotNull(result);
        assertEquals("john.smith@example.com", result.getLecturerEmail());
        assertEquals("johnsmith", result.getUsername());
        assertEquals(1, result.getDepartmentId());
        verify(lecturerRepository).findById(1);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        when(lecturerRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            lecturerService.findById(999);
        });

        assertTrue(exception.getMessage().contains("Lecturer not found"));
        verify(lecturerRepository).findById(999);
    }

    @Test
    void save_ShouldSaveAndRegisterLecturer() {
        // Given
        when(modelMapper.map(any(LecturerCreateDTO.class), eq(Lecturer.class))).thenReturn(testLecturer);
        when(lecturerRepository.save(any(Lecturer.class))).thenReturn(testLecturer);
        doNothing().when(userService).registerLecturerUser(anyString(), anyString(), anyString());

        // When
        Lecturer result = lecturerService.save(testLecturerCreateDTO);

        // Then
        assertNotNull(result);
        verify(modelMapper).map(eq(testLecturerCreateDTO), eq(Lecturer.class));
        verify(lecturerRepository).save(any(Lecturer.class));
        verify(userService).registerLecturerUser(
                eq(testLecturerCreateDTO.getUsername()),
                eq(testLecturerCreateDTO.getLecturerEmail()),
                eq(testLecturerCreateDTO.getPassword())
        );
    }

    @Test
    void saveLecturersList_ShouldSaveAllLecturers() {
        // Given
        List<LecturerCreateDTO> dtoList = Arrays.asList(testLecturerCreateDTO);
        when(modelMapper.map(any(LecturerCreateDTO.class), eq(Lecturer.class))).thenReturn(testLecturer);
        when(lecturerRepository.save(any(Lecturer.class))).thenReturn(testLecturer);
        doNothing().when(userService).registerLecturerUser(anyString(), anyString(), anyString());

        // When
        lecturerService.saveLecturersList(dtoList);

        // Then
        verify(modelMapper).map(eq(testLecturerCreateDTO), eq(Lecturer.class));
        verify(lecturerRepository).save(any(Lecturer.class));
        verify(userService).registerLecturerUser(
                eq(testLecturerCreateDTO.getUsername()),
                eq(testLecturerCreateDTO.getLecturerEmail()),
                eq(testLecturerCreateDTO.getPassword())
        );
    }

    @Test
    void deleteById_ShouldDeleteLecturer() {
        // Given
        doNothing().when(lecturerRepository).deleteById(1);

        // When
        lecturerService.deleteById(1);

        // Then
        verify(lecturerRepository).deleteById(1);
    }

    @Test
    void findByDepartmentId_ShouldReturnMatchingLecturers() {
        // Given
        List<Lecturer> lecturers = Arrays.asList(testLecturer);
        when(lecturerRepository.findByDepartmentId(1)).thenReturn(lecturers);
        when(modelMapper.map(any(Lecturer.class), eq(LecturerDTO.class))).thenReturn(testLecturerDTO);

        // When
        List<LecturerDTO> result = lecturerService.findByDepartmentId(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("john.smith@example.com", result.get(0).getLecturerEmail());
        assertEquals("johnsmith", result.get(0).getUsername());
        assertEquals(1, result.get(0).getDepartmentId());
        verify(lecturerRepository).findByDepartmentId(1);
    }
}