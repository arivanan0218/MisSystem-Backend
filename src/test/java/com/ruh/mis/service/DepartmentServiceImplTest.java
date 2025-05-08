package com.ruh.mis.service;

import com.ruh.mis.model.Department;
import com.ruh.mis.model.DTO.DepartmentCreateDTO;
import com.ruh.mis.model.DTO.DepartmentDTO;
import com.ruh.mis.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    // Test data
    private Department testDepartment;
    private DepartmentCreateDTO testDepartmentCreateDTO;
    private DepartmentDTO testDepartmentDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testDepartment = new Department();
        testDepartment.setId(1);
        testDepartment.setDepartmentName("Computer Science");
        testDepartment.setDepartmentCode("CS");
        testDepartment.setHodName("Dr. John Smith");

        testDepartmentCreateDTO = new DepartmentCreateDTO();
        testDepartmentCreateDTO.setDepartmentName("Computer Science");
        testDepartmentCreateDTO.setDepartmentCode("CS");
        testDepartmentCreateDTO.setHodName("Dr. John Smith");

        testDepartmentDTO = new DepartmentDTO();
        testDepartmentDTO.setId(1);
        testDepartmentDTO.setDepartmentName("Computer Science");
        testDepartmentDTO.setDepartmentCode("CS");
        testDepartmentDTO.setHodName("Dr. John Smith");
    }

    @Test
    void findAll_ShouldReturnAllDepartments() {
        // Given
        List<Department> departments = List.of(testDepartment);
        when(departmentRepository.findAll()).thenReturn(departments);

        // When
        List<DepartmentDTO> result = departmentService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Computer Science", result.get(0).getDepartmentName());
        assertEquals("CS", result.get(0).getDepartmentCode());
        assertEquals("Dr. John Smith", result.get(0).getHodName());
        verify(departmentRepository).findAll();
    }

    @Test
    void findById_WithValidId_ShouldReturnDepartment() {
        // Given
        when(departmentRepository.findById(1)).thenReturn(Optional.of(testDepartment));

        // When
        DepartmentDTO result = departmentService.findById(1);

        // Then
        assertNotNull(result);
        assertEquals("Computer Science", result.getDepartmentName());
        assertEquals("CS", result.getDepartmentCode());
        assertEquals("Dr. John Smith", result.getHodName());
        verify(departmentRepository).findById(1);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        when(departmentRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            departmentService.findById(999);
        });

        assertTrue(exception.getMessage().contains("Department not found"));
        verify(departmentRepository).findById(999);
    }

    @Test
    void save_ShouldSaveAndReturnDepartment() {
        // Given
        when(departmentRepository.save(any(Department.class))).thenReturn(testDepartment);

        // When
        Department result = departmentService.save(testDepartmentCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Computer Science", result.getDepartmentName());
        assertEquals("CS", result.getDepartmentCode());
        assertEquals("Dr. John Smith", result.getHodName());
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void deleteById_ShouldDeleteDepartment() {
        // Given
        doNothing().when(departmentRepository).deleteById(1);

        // When
        departmentService.deleteById(1);

        // Then
        verify(departmentRepository).deleteById(1);
    }

    @Test
    void update_WithValidId_ShouldUpdateAndReturnDepartment() {
        // Given
        when(departmentRepository.findById(1)).thenReturn(Optional.of(testDepartment));

        // Create updated department
        Department updatedDepartment = new Department();
        updatedDepartment.setId(1);
        updatedDepartment.setDepartmentName("Updated Department");
        updatedDepartment.setDepartmentCode("UD");
        updatedDepartment.setHodName("Dr. Jane Doe");

        when(departmentRepository.save(any(Department.class))).thenReturn(updatedDepartment);

        // Create update DTO
        DepartmentCreateDTO updateDTO = new DepartmentCreateDTO();
        updateDTO.setDepartmentName("Updated Department");
        updateDTO.setDepartmentCode("UD");
        updateDTO.setHodName("Dr. Jane Doe");

        // When
        DepartmentDTO result = departmentService.update(1, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated Department", result.getDepartmentName());
        assertEquals("UD", result.getDepartmentCode());
        assertEquals("Dr. Jane Doe", result.getHodName());
        verify(departmentRepository).findById(1);
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Given
        when(departmentRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            departmentService.update(999, testDepartmentCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Department not found"));
        verify(departmentRepository).findById(999);
        verify(departmentRepository, never()).save(any(Department.class));
    }
}