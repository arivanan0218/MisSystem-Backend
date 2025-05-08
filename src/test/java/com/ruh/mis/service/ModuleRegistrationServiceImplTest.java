package com.ruh.mis.service;

import com.ruh.mis.model.*;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;
import com.ruh.mis.model.DTO.TakenModuleDTO;
import com.ruh.mis.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleRegistrationServiceImplTest {

    @Mock
    private ModuleRegistrationRepository registrationRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private IntakeRepository intakeRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ModuleRegistrationServiceImpl moduleRegistrationService;

    // Test data
    private Student testStudent;
    private Semester testSemester;
    private Intake testIntake;
    private Department testDepartment;
    private com.ruh.mis.model.Module testModule;
    private ModuleRegistration testRegistration;
    private ModuleRegistrationRequestDTO testRequestDTO;
    private TakenModuleDTO testTakenModuleDTO;

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

        testModule = new com.ruh.mis.model.Module();
        testModule.setId(1);
        testModule.setModuleName("Programming Fundamentals");
        testModule.setModuleCode("CS101");

        testStudent = new Student();
        testStudent.setId(1);
        testStudent.setStudentRegNo("12345");
        testStudent.setStudentName("John Doe");
        testStudent.setDepartment(testDepartment);

        testRegistration = new ModuleRegistration();
        testRegistration.setId(1);
        testRegistration.setStudent(testStudent);
        testRegistration.setSemester(testSemester);
        testRegistration.setIntake(testIntake);
        testRegistration.setDepartment(testDepartment);
        testRegistration.setModule(testModule);
        testRegistration.setGrade("G");
        testRegistration.setStatus("Taken");

        // Create DTOs for testing
        testTakenModuleDTO = new TakenModuleDTO();
        testTakenModuleDTO.setModuleId(1);
        testTakenModuleDTO.setGpaStatus("G");

        testRequestDTO = new ModuleRegistrationRequestDTO();
        testRequestDTO.setStudentId(1);
        testRequestDTO.setSemesterId(1);
        testRequestDTO.setIntakeId(1);
        testRequestDTO.setDepartmentId(1);
        testRequestDTO.setTakenModules(List.of(testTakenModuleDTO));
    }

    @Test
    void registerModules_WithNewRegistration_ShouldCreateAndSaveRegistration() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(semesterRepository.findById(1)).thenReturn(Optional.of(testSemester));
        when(intakeRepository.findById(1)).thenReturn(Optional.of(testIntake));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(testDepartment));
        when(moduleRepository.findById(1)).thenReturn(Optional.of(testModule));
        when(registrationRepository.findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(1, 1, 1, 1, 1))
                .thenReturn(Optional.empty());
        when(registrationRepository.save(any(ModuleRegistration.class))).thenReturn(testRegistration);

        // When
        moduleRegistrationService.registerModules(testRequestDTO);

        // Then
        verify(studentRepository).findById(1);
        verify(semesterRepository).findById(1);
        verify(intakeRepository).findById(1);
        verify(departmentRepository).findById(1);
        verify(moduleRepository).findById(1);
        verify(registrationRepository).findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(1, 1, 1, 1, 1);
        verify(registrationRepository).save(any(ModuleRegistration.class));
    }

    @Test
    void registerModules_WithExistingRegistration_ShouldUpdateAndSaveRegistration() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(semesterRepository.findById(1)).thenReturn(Optional.of(testSemester));
        when(intakeRepository.findById(1)).thenReturn(Optional.of(testIntake));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(testDepartment));
        when(moduleRepository.findById(1)).thenReturn(Optional.of(testModule));
        when(registrationRepository.findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(1, 1, 1, 1, 1))
                .thenReturn(Optional.of(testRegistration));
        when(registrationRepository.save(any(ModuleRegistration.class))).thenReturn(testRegistration);

        // When
        moduleRegistrationService.registerModules(testRequestDTO);

        // Then
        verify(studentRepository).findById(1);
        verify(semesterRepository).findById(1);
        verify(intakeRepository).findById(1);
        verify(departmentRepository).findById(1);
        verify(moduleRepository).findById(1);
        verify(registrationRepository).findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(1, 1, 1, 1, 1);
        verify(registrationRepository).save(testRegistration);
    }

    @Test
    void registerModules_WithDifferentGpaStatus_ShouldSetCorrectGradeAndStatus() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(semesterRepository.findById(1)).thenReturn(Optional.of(testSemester));
        when(intakeRepository.findById(1)).thenReturn(Optional.of(testIntake));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(testDepartment));
        when(moduleRepository.findById(1)).thenReturn(Optional.of(testModule));
        when(registrationRepository.findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(1, 1, 1, 1, 1))
                .thenReturn(Optional.empty());

        // Create a specific test for the G status
        testTakenModuleDTO.setGpaStatus("G");
        when(registrationRepository.save(any(ModuleRegistration.class))).then(invocation -> {
            ModuleRegistration saved = invocation.getArgument(0);
            assertEquals("G", saved.getGrade());
            assertEquals("Taken", saved.getStatus());
            return saved;
        });

        moduleRegistrationService.registerModules(testRequestDTO);

        // Create a specific test for the - status
        testTakenModuleDTO.setGpaStatus("-");
        when(registrationRepository.save(any(ModuleRegistration.class))).then(invocation -> {
            ModuleRegistration saved = invocation.getArgument(0);
            assertEquals("-", saved.getGrade());
            assertEquals("Not-Taken", saved.getStatus());
            return saved;
        });

        moduleRegistrationService.registerModules(testRequestDTO);

        // Create a specific test for the N status
        testTakenModuleDTO.setGpaStatus("N");
        when(registrationRepository.save(any(ModuleRegistration.class))).then(invocation -> {
            ModuleRegistration saved = invocation.getArgument(0);
            assertEquals("N", saved.getGrade());
            assertEquals("Taken", saved.getStatus());
            return saved;
        });

        moduleRegistrationService.registerModules(testRequestDTO);

        // Then
        verify(registrationRepository, times(3)).save(any(ModuleRegistration.class));
    }

    @Test
    void registerModules_WithInvalidGpaStatus_ShouldThrowException() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(semesterRepository.findById(1)).thenReturn(Optional.of(testSemester));
        when(intakeRepository.findById(1)).thenReturn(Optional.of(testIntake));
        when(departmentRepository.findById(1)).thenReturn(Optional.of(testDepartment));
        when(moduleRepository.findById(1)).thenReturn(Optional.of(testModule));
        when(registrationRepository.findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(1, 1, 1, 1, 1))
                .thenReturn(Optional.empty());

        // Invalid GPA status
        testTakenModuleDTO.setGpaStatus("INVALID");

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            moduleRegistrationService.registerModules(testRequestDTO);
        });

        assertTrue(exception.getMessage().contains("Invalid GPA Status"));
    }

    @Test
    void registerModules_WithNonExistentStudent_ShouldThrowException() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            moduleRegistrationService.registerModules(testRequestDTO);
        });

        assertTrue(exception.getMessage().contains("Student not found"));
        verify(studentRepository).findById(1);
        verify(registrationRepository, never()).save(any(ModuleRegistration.class));
    }

    @Test
    void getRegistrationDetailsForStudent_ShouldReturnCorrectDetails() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.of(testStudent));
        when(registrationRepository.findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentId(1, 1, 1, 1))
                .thenReturn(List.of(testRegistration));

        // When
        ModuleRegistrationResponseDTO response = moduleRegistrationService.getRegistrationDetailsForStudent(1, 1, 1, 1);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("John Doe", response.getStudentName());
        assertEquals("12345", response.getStudentRegNo());
        assertEquals("Computer Science", response.getDepartmentName());

        assertEquals(1, response.getModules().size());
        Map<String, String> moduleDetails = response.getModules().get(0);
        assertEquals("1", moduleDetails.get("moduleId"));
        assertEquals("CS101", moduleDetails.get("moduleCode"));
        assertEquals("Programming Fundamentals", moduleDetails.get("moduleName"));
        assertEquals("Taken", moduleDetails.get("status"));
        assertEquals("G", moduleDetails.get("grade"));

        verify(studentRepository).findById(1);
        verify(registrationRepository).findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentId(1, 1, 1, 1);
    }

    @Test
    void getRegistrationDetailsForStudent_WithNonExistentStudent_ShouldThrowException() {
        // Given
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            moduleRegistrationService.getRegistrationDetailsForStudent(1, 1, 1, 1);
        });

        assertTrue(exception.getMessage().contains("Student not found"));
        verify(studentRepository).findById(1);
        verify(registrationRepository, never()).findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentId(
                anyInt(), anyInt(), anyInt(), anyInt());
    }
}