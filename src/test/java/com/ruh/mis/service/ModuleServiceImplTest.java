package com.ruh.mis.service;

import com.ruh.mis.model.Department;
import com.ruh.mis.model.GPAStatus;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.repository.ModuleRepository;
import com.ruh.mis.repository.StudentRepository;
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
public class ModuleServiceImplTest {

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ModuleServiceImpl moduleService;

    // Test data
    private Module testModule;
    private Department testDepartment;
    private Intake testIntake;
    private Semester testSemester;
    private ModuleCreateDTO testModuleCreateDTO;
    private ModuleDTO testModuleDTO;

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

        testModule = new Module();
        testModule.setId(1);
        testModule.setModuleName("Programming Fundamentals");
        testModule.setModuleCode("CS101");
        testModule.setCredit(3);
        testModule.setGpaStatus(GPAStatus.GPA); // Assuming GPAStatus.GPA represents true
        testModule.setModuleCoordinator("Dr. John Doe");
        testModule.setDepartment(testDepartment);
        testModule.setIntake(testIntake);
        testModule.setSemester(testSemester);

        // Create DTO for testing
        testModuleCreateDTO = new ModuleCreateDTO();
        testModuleCreateDTO.setModuleName("New Module");
        testModuleCreateDTO.setModuleCode("CS202");
        testModuleCreateDTO.setCredit(4);
        testModuleCreateDTO.setGpaStatus(GPAStatus.GPA); // Using enum value
        testModuleCreateDTO.setModuleCoordinator("Dr. Jane Smith");
        testModuleCreateDTO.setDepartmentId(1);
        testModuleCreateDTO.setIntakeId(1);
        testModuleCreateDTO.setSemesterId(1);

        // Create a DTO for response testing
        testModuleDTO = new ModuleDTO();
        testModuleDTO.setId(1);
        testModuleDTO.setModuleName("Programming Fundamentals");
        testModuleDTO.setModuleCode("CS101");
        testModuleDTO.setCredit(3);
        testModuleDTO.setGpaStatus(GPAStatus.GPA); // Using enum value
        testModuleDTO.setModuleCoordinator("Dr. John Doe");
        testModuleDTO.setDepartmentId(1);
        testModuleDTO.setIntakeId(1);
        testModuleDTO.setSemesterId(1);
    }

    @Test
    void findAll_ShouldReturnAllModules() {
        // Given
        List<Module> modules = Arrays.asList(testModule);
        when(moduleRepository.findAll()).thenReturn(modules);
        when(modelMapper.map(any(Module.class), eq(ModuleDTO.class))).thenReturn(testModuleDTO);

        // When
        List<ModuleDTO> result = moduleService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Programming Fundamentals", result.get(0).getModuleName());
        assertEquals("CS101", result.get(0).getModuleCode());
        assertEquals(3, result.get(0).getCredit());
        assertEquals(GPAStatus.GPA, result.get(0).getGpaStatus()); // Using enum comparison
        assertEquals("Dr. John Doe", result.get(0).getModuleCoordinator());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals(1, result.get(0).getIntakeId());
        assertEquals(1, result.get(0).getSemesterId());
        verify(moduleRepository).findAll();
    }

    @Test
    void findById_WithValidId_ShouldReturnModule() {
        // Given
        when(moduleRepository.findById(1)).thenReturn(Optional.of(testModule));
        when(modelMapper.map(any(Module.class), eq(ModuleDTO.class))).thenReturn(testModuleDTO);

        // When
        ModuleDTO result = moduleService.findById(1);

        // Then
        assertNotNull(result);
        assertEquals("Programming Fundamentals", result.getModuleName());
        assertEquals("CS101", result.getModuleCode());
        assertEquals(3, result.getCredit());
        assertEquals(GPAStatus.GPA, result.getGpaStatus()); // Using enum comparison
        assertEquals("Dr. John Doe", result.getModuleCoordinator());
        assertEquals(1, result.getDepartmentId());
        assertEquals(1, result.getIntakeId());
        assertEquals(1, result.getSemesterId());
        verify(moduleRepository).findById(1);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Given
        when(moduleRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            moduleService.findById(999);
        });

        assertTrue(exception.getMessage().contains("Module not found"));
        verify(moduleRepository).findById(999);
    }

    @Test
    void getModuleByDepartmentIdAndIntakeIdAndSemesterId_ShouldReturnMatchingModules() {
        // Given
        List<Module> modules = Arrays.asList(testModule);
        when(moduleRepository.findByDepartmentIdAndIntakeIdAndSemesterId(1, 1, 1)).thenReturn(modules);
        when(modelMapper.map(any(Module.class), eq(ModuleDTO.class))).thenReturn(testModuleDTO);

        // When
        List<ModuleDTO> result = moduleService.getModuleByDepartmentIdAndIntakeIdAndSemesterId(1, 1, 1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Programming Fundamentals", result.get(0).getModuleName());
        assertEquals("CS101", result.get(0).getModuleCode());
        assertEquals(3, result.get(0).getCredit());
        assertEquals(GPAStatus.GPA, result.get(0).getGpaStatus()); // Using enum comparison
        assertEquals("Dr. John Doe", result.get(0).getModuleCoordinator());
        assertEquals(1, result.get(0).getDepartmentId());
        assertEquals(1, result.get(0).getIntakeId());
        assertEquals(1, result.get(0).getSemesterId());
        verify(moduleRepository).findByDepartmentIdAndIntakeIdAndSemesterId(1, 1, 1);
    }

    @Test
    void save_ShouldSaveAndReturnModule() {
        // Given
        Module mappedModule = new Module();
        mappedModule.setModuleName("New Module");
        mappedModule.setModuleCode("CS202");
        mappedModule.setCredit(4);
        mappedModule.setGpaStatus(GPAStatus.GPA); // Using enum value
        mappedModule.setModuleCoordinator("Dr. Jane Smith");

        when(modelMapper.map(eq(testModuleCreateDTO), eq(Module.class))).thenReturn(mappedModule);
        when(moduleRepository.save(any(Module.class))).thenReturn(mappedModule);

        // When
        Module result = moduleService.save(testModuleCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("New Module", result.getModuleName());
        assertEquals("CS202", result.getModuleCode());
        assertEquals(4, result.getCredit());
        assertEquals(GPAStatus.GPA, result.getGpaStatus()); // Using enum comparison
        assertEquals("Dr. Jane Smith", result.getModuleCoordinator());
        verify(modelMapper).map(eq(testModuleCreateDTO), eq(Module.class));
        verify(moduleRepository).save(any(Module.class));
    }

    @Test
    void update_WithValidId_ShouldUpdateAndReturnModule() {
        // Given
        when(moduleRepository.findById(1)).thenReturn(Optional.of(testModule));
        when(moduleRepository.save(any(Module.class))).thenReturn(testModule);
        when(modelMapper.map(any(Module.class), eq(ModuleDTO.class))).thenReturn(testModuleDTO);

        testModuleCreateDTO.setModuleName("Updated Module");
        testModuleCreateDTO.setModuleCode("CS303");
        testModuleCreateDTO.setCredit(5);
        testModuleCreateDTO.setGpaStatus(GPAStatus.NGPA); // Assuming GPAStatus.NGPA represents false
        testModuleCreateDTO.setModuleCoordinator("Dr. Robert Johnson");

        // When
        ModuleDTO result = moduleService.update(1, testModuleCreateDTO);

        // Then
        assertNotNull(result);
        // Verify the entity was updated with new values
        assertEquals("Updated Module", testModule.getModuleName());
        assertEquals("CS303", testModule.getModuleCode());
        assertEquals(5, testModule.getCredit());
        assertEquals(GPAStatus.NGPA, testModule.getGpaStatus()); // Using enum comparison
        assertEquals("Dr. Robert Johnson", testModule.getModuleCoordinator());
        verify(moduleRepository).findById(1);
        verify(moduleRepository).save(testModule);
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Given
        when(moduleRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            moduleService.update(999, testModuleCreateDTO);
        });

        assertTrue(exception.getMessage().contains("Module not found"));
        verify(moduleRepository).findById(999);
        verify(moduleRepository, never()).save(any(Module.class));
    }

    @Test
    void deleteById_ShouldDeleteModule() {
        // Given
        doNothing().when(moduleRepository).deleteById(1);

        // When
        moduleService.deleteById(1);

        // Then
        verify(moduleRepository).deleteById(1);
    }
}