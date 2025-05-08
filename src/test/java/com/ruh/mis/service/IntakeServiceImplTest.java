package com.ruh.mis.service;

import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.DTO.IntakeCreateDTO;
import com.ruh.mis.model.DTO.IntakeDTO;
import com.ruh.mis.repository.IntakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IntakeServiceImplTest {

    @Mock
    private IntakeRepository intakeRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private IntakeServiceImpl intakeService;

    // Test data
    private Intake testIntake;
    private Department testDepartment;
    private IntakeCreateDTO testIntakeCreateDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testDepartment = new Department();
        testDepartment.setId(1);
        testDepartment.setDepartmentName("Computer Science");

        testIntake = new Intake();
        testIntake.setId(1);
        // Change from Integer to String
        testIntake.setIntakeYear("2022");
        testIntake.setBatch("A");
        testIntake.setDepartment(testDepartment);

        // Create DTO for testing
        testIntakeCreateDTO = new IntakeCreateDTO();
        // Change from Integer to String
        testIntakeCreateDTO.setIntakeYear("2023");
        testIntakeCreateDTO.setBatch("B");
        testIntakeCreateDTO.setDepartmentId(1);
    }

    @Test
    void findAll_ShouldReturnAllIntakes() {
        // Given
        List<Intake> intakes = Arrays.asList(testIntake);
        when(intakeRepository.findAll()).thenReturn(intakes);

        // When
        List<IntakeDTO> result = intakeService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        // Compare string values instead of integers
        assertEquals("2022", result.get(0).getIntakeYear());
        assertEquals("A", result.get(0).getBatch());
        assertEquals(1, result.get(0).getDepartmentId());
        verify(intakeRepository).findAll();
    }

    @Test
    void findById_WithValidId_ShouldReturnIntake() {
        // Given
        when(intakeRepository.findById(1)).thenReturn(Optional.of(testIntake));

        // When
        IntakeDTO result = intakeService.findById(1);

        // Then
        assertNotNull(result);
        // Compare string values instead of integers
        assertEquals("2022", result.getIntakeYear());
        assertEquals("A", result.getBatch());
        assertEquals(1, result.getDepartmentId());
        verify(intakeRepository).findById(1);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        when(intakeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            intakeService.findById(999);
        });

        assertTrue(exception.getMessage().contains("Intake not found"));
        verify(intakeRepository).findById(999);
    }

    @Test
    void getIntakesByDepartmentId_ShouldReturnMatchingIntakes() {
        // Given
        List<Intake> intakes = Arrays.asList(testIntake);
        when(intakeRepository.findByDepartmentId(1)).thenReturn(intakes);

        // When
        List<IntakeDTO> result = intakeService.getIntakesByDepartmentId(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        // Compare string values instead of integers
        assertEquals("2022", result.get(0).getIntakeYear());
        assertEquals("A", result.get(0).getBatch());
        assertEquals(1, result.get(0).getDepartmentId());
        verify(intakeRepository).findByDepartmentId(1);
    }

    @Test
    void save_ShouldSaveAndReturnIntake() {
        // Given
        Intake mappedIntake = new Intake();
        // Use string instead of integer
        mappedIntake.setIntakeYear("2023");
        mappedIntake.setBatch("B");

        when(intakeRepository.save(any(Intake.class))).thenReturn(mappedIntake);

        // When
        Intake result = intakeService.save(testIntakeCreateDTO);

        // Then
        assertNotNull(result);
        // Compare string values instead of integers
        assertEquals("2023", result.getIntakeYear());
        assertEquals("B", result.getBatch());
        verify(intakeRepository).save(any(Intake.class));
    }

    @Test
    void update_WithValidId_ShouldUpdateAndReturnIntake() {
        // Given
        when(intakeRepository.findById(1)).thenReturn(Optional.of(testIntake));
        when(intakeRepository.save(any(Intake.class))).thenReturn(testIntake);

        // Use string instead of integer
        testIntakeCreateDTO.setIntakeYear("2024");
        testIntakeCreateDTO.setBatch("C");

        // When
        IntakeDTO result = intakeService.update(1, testIntakeCreateDTO);

        // Then
        assertNotNull(result);
        // Compare string values instead of integers
        assertEquals("2024", testIntake.getIntakeYear());
        assertEquals("C", testIntake.getBatch());
        verify(intakeRepository).findById(1);
        verify(intakeRepository).save(testIntake);
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Given
        when(intakeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            intakeService.update(999, testIntakeCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Intake not found"));
        verify(intakeRepository).findById(999);
        verify(intakeRepository, never()).save(any(Intake.class));
    }

    @Test
    void deleteById_ShouldDeleteIntake() {
        // Given
        doNothing().when(intakeRepository).deleteById(1);

        // When
        intakeService.deleteById(1);

        // Then
        verify(intakeRepository).deleteById(1);
    }
}