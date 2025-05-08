package com.ruh.mis.service;

import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.SemesterDTO;
import com.ruh.mis.repository.SemesterRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SemesterServiceImplTest {

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SemesterServiceImpl semesterService;

    // Test data
    private Semester testSemester;
    private Department testDepartment;
    private Intake testIntake;
    private SemesterCreateDTO testSemesterCreateDTO;
    private SemesterDTO testSemesterDTO;

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
        testSemester.setSemesterYear("2022/2023");
        testSemester.setSemesterDuration("4 months");
        testSemester.setDepartment(testDepartment);
        testSemester.setIntake(testIntake);

        // Create DTO for testing
        testSemesterCreateDTO = new SemesterCreateDTO();
        testSemesterCreateDTO.setSemesterName("New Semester");
        testSemesterCreateDTO.setSemesterYear("2023/2024");
        testSemesterCreateDTO.setSemesterDuration("3 months");
        testSemesterCreateDTO.setDepartmentId(1);
        testSemesterCreateDTO.setIntakeId(1);

        // Create a DTO for response testing
        testSemesterDTO = new SemesterDTO();
        testSemesterDTO.setId(1);
        testSemesterDTO.setSemesterName("Semester 1");
        testSemesterDTO.setSemesterYear("2022/2023");
        testSemesterDTO.setSemesterDuration("4 months");
        testSemesterDTO.setDepartmentId(1);
        testSemesterDTO.setIntakeId(1);
    }

    @Test
    void findAll_ShouldReturnAllSemesters() {
        // Given
        List<Semester> semesters = Arrays.asList(testSemester);
        when(semesterRepository.findAll()).thenReturn(semesters);
        when(modelMapper.map(any(Semester.class), eq(SemesterDTO.class))).thenReturn(testSemesterDTO);

        // When
        List<SemesterDTO> result = semesterService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Semester 1", result.get(0).getSemesterName());
        assertEquals("2022/2023", result.get(0).getSemesterYear());
        assertEquals("4 months", result.get(0).getSemesterDuration());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals(1, result.get(0).getIntakeId());
        verify(semesterRepository).findAll();
    }

    @Test
    void findById_WithValidId_ShouldReturnSemester() {
        // Given
        when(semesterRepository.findById(1)).thenReturn(Optional.of(testSemester));
        when(modelMapper.map(any(Semester.class), eq(SemesterDTO.class))).thenReturn(testSemesterDTO);

        // When
        SemesterDTO result = semesterService.findById(1);

        // Then
        assertNotNull(result);
        assertEquals("Semester 1", result.getSemesterName());
        assertEquals("2022/2023", result.getSemesterYear());
        assertEquals("4 months", result.getSemesterDuration());
        assertEquals(1, result.getDepartmentId());
        assertEquals(1, result.getIntakeId());
        verify(semesterRepository).findById(1);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        when(semesterRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            semesterService.findById(999);
        });

        assertTrue(exception.getMessage().contains("Semester not found"));
        verify(semesterRepository).findById(999);
    }

    @Test
    void getSemestersByDepartmentIdAndIntakeId_ShouldReturnMatchingSemesters() {
        // Given
        List<Semester> semesters = Arrays.asList(testSemester);
        when(semesterRepository.findByDepartmentIdAndIntakeId(1, 1)).thenReturn(semesters);
        when(modelMapper.map(any(Semester.class), eq(SemesterDTO.class))).thenReturn(testSemesterDTO);

        // When
        List<SemesterDTO> result = semesterService.getSemestersByDepartmentIdAndIntakeId(1, 1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Semester 1", result.get(0).getSemesterName());
        assertEquals("2022/2023", result.get(0).getSemesterYear());
        assertEquals("4 months", result.get(0).getSemesterDuration());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals(1, result.get(0).getIntakeId());
        verify(semesterRepository).findByDepartmentIdAndIntakeId(1, 1);
    }

    @Test
    void save_ShouldSaveAndReturnSemester() {
        // Given
        Semester mappedSemester = new Semester();
        mappedSemester.setSemesterName("New Semester");
        mappedSemester.setSemesterYear("2023/2024");
        mappedSemester.setSemesterDuration("3 months");

        when(modelMapper.map(eq(testSemesterCreateDTO), eq(Semester.class))).thenReturn(mappedSemester);
        when(semesterRepository.save(any(Semester.class))).thenReturn(mappedSemester);

        // When
        Semester result = semesterService.save(testSemesterCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("New Semester", result.getSemesterName());
        assertEquals("2023/2024", result.getSemesterYear());
        assertEquals("3 months", result.getSemesterDuration());
        verify(modelMapper).map(eq(testSemesterCreateDTO), eq(Semester.class));
        verify(semesterRepository).save(any(Semester.class));
    }

    @Test
    void update_WithValidId_ShouldUpdateAndReturnSemester() {
        // Given
        when(semesterRepository.findById(1)).thenReturn(Optional.of(testSemester));
        when(semesterRepository.save(any(Semester.class))).thenReturn(testSemester);
        when(modelMapper.map(any(Semester.class), eq(SemesterDTO.class))).thenReturn(testSemesterDTO);

        testSemesterCreateDTO.setSemesterName("Updated Semester");
        testSemesterCreateDTO.setSemesterYear("2024/2025");
        testSemesterCreateDTO.setSemesterDuration("5 months");

        // When
        SemesterDTO result = semesterService.update(1, testSemesterCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated Semester", testSemester.getSemesterName());
        assertEquals("2024/2025", testSemester.getSemesterYear());
        assertEquals("5 months", testSemester.getSemesterDuration());
        verify(semesterRepository).findById(1);
        verify(semesterRepository).save(testSemester);
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Given
        when(semesterRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            semesterService.update(999, testSemesterCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Semester not found"));
        verify(semesterRepository).findById(999);
        verify(semesterRepository, never()).save(any(Semester.class));
    }

    @Test
    void deleteById_ShouldDeleteSemester() {
        // Given
        doNothing().when(semesterRepository).deleteById(1);

        // When
        semesterService.deleteById(1);

        // Then
        verify(semesterRepository).deleteById(1);
    }
}