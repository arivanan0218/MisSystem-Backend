package com.ruh.mis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;
import com.ruh.mis.model.DTO.TakenModuleDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.GPAStatus;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.ModuleRegistration;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.IntakeRepository;
import com.ruh.mis.repository.ModuleRegistrationRepository;
import com.ruh.mis.repository.ModuleRepository;
import com.ruh.mis.repository.SemesterRepository;
import com.ruh.mis.repository.StudentRepository;

@Service
public class ModuleRegistrationServiceImpl implements ModuleRegistrationService {

    @Autowired
    private ModuleRegistrationRepository moduleRegistrationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void registerModules(ModuleRegistrationRequestDTO requestDTO) {
        // Fetch required entities
        Student student = studentRepository.findById(requestDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + requestDTO.getStudentId()));
        
        Semester semester = semesterRepository.findById(requestDTO.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found: " + requestDTO.getSemesterId()));
        
        Intake intake = intakeRepository.findById(requestDTO.getIntakeId())
                .orElseThrow(() -> new RuntimeException("Intake not found: " + requestDTO.getIntakeId()));
        
        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found: " + requestDTO.getDepartmentId()));
        
        for (TakenModuleDTO takenModule : requestDTO.getTakenModules()) {
            com.ruh.mis.model.Module module = moduleRepository.findById(takenModule.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found: " + takenModule.getModuleId()));
            
            // Check if registration already exists
            Optional<ModuleRegistration> existingReg = moduleRegistrationRepository
                    .findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(
                            requestDTO.getStudentId(),
                            requestDTO.getSemesterId(),
                            requestDTO.getIntakeId(),
                            requestDTO.getDepartmentId(),
                            takenModule.getModuleId());
            
            if (existingReg.isPresent()) {
                // Update existing registration (status, grade)
                ModuleRegistration registration = existingReg.get();
                
                // Update grade and status based on GPA status code
                switch (takenModule.getGpaStatus()) {
                    case "G" -> {
                        registration.setGrade("G");
                        registration.setStatus("Taken");
                    }
                    case "-" -> {
                        registration.setGrade("-");
                        registration.setStatus("Not-Taken");
                    }
                    case "N" -> {
                        registration.setGrade("N");
                        registration.setStatus("Taken");
                    }
                    default -> throw new RuntimeException("Invalid GPA Status: " + takenModule.getGpaStatus());
                }
                
                moduleRegistrationRepository.save(registration);
            } else {
                // Create new registration
                ModuleRegistration registration = new ModuleRegistration();
                registration.setStudent(student);
                registration.setSemester(semester);
                registration.setIntake(intake);
                registration.setDepartment(department);
                registration.setModule(module);
                
                // Set grade and status based on GPA status code
                switch (takenModule.getGpaStatus()) {
                    case "G" -> {
                        registration.setGrade("G");
                        registration.setStatus("Taken");
                    }
                    case "-" -> {
                        registration.setGrade("-");
                        registration.setStatus("Not-Taken");
                    }
                    case "N" -> {
                        registration.setGrade("N");
                        registration.setStatus("Taken");
                    }
                    default -> throw new RuntimeException("Invalid GPA Status: " + takenModule.getGpaStatus());
                }
                
                moduleRegistrationRepository.save(registration);
            }
        }
    }

    @Override
    public ModuleRegistrationResponseDTO getRegistrationDetailsForStudent(
            int studentId, int semesterId, int intakeId, int departmentId) {
        
        // Fetch student details
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));
        
        // Fetch module registrations
        List<ModuleRegistration> registrations = moduleRegistrationRepository
                .findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentId(
                        studentId, semesterId, intakeId, departmentId);
        
        // Create response DTO
        ModuleRegistrationResponseDTO responseDTO = new ModuleRegistrationResponseDTO();
        responseDTO.setId(student.getId());
        responseDTO.setStudentName(student.getStudentName());
        responseDTO.setStudentRegNo(student.getStudentRegNo());
        responseDTO.setDepartmentName(department.getDepartmentName());
        
        // Map modules
        List<Map<String, String>> modules = registrations.stream()
                .map(reg -> {
                    Map<String, String> moduleMap = new HashMap<>();
                    moduleMap.put("id", String.valueOf(reg.getModule().getId()));
                    moduleMap.put("code", reg.getModule().getModuleCode());
                    moduleMap.put("name", reg.getModule().getModuleName());
                    moduleMap.put("status", reg.getStatus());
                    moduleMap.put("grade", reg.getGrade());
                    return moduleMap;
                })
                .collect(Collectors.toList());
        
        responseDTO.setModules(modules);
        
        return responseDTO;
    }
    
    @Override
    public List<ModuleRegistrationDTO> getRegistrationsByModuleId(int moduleId) {
        // Fetch registrations for the module
        List<ModuleRegistration> registrations = moduleRegistrationRepository.findByModuleId(moduleId);
        
        // Map to DTOs
        return registrations.stream()
                .map(registration -> {
                    ModuleRegistrationDTO dto = new ModuleRegistrationDTO();
                    dto.setId(registration.getId());
                    dto.setSemesterId(registration.getSemester().getId());
                    dto.setIntakeId(registration.getIntake().getId());
                    dto.setDepartmentId(registration.getDepartment().getId());
                    
                    // Student information
                    Student student = registration.getStudent();
                    dto.setStudentName(student.getStudentName());
                    dto.setStudentRegNo(student.getStudentRegNo());
                    
                    // Module information
                    com.ruh.mis.model.Module module = registration.getModule();
                    dto.setModuleCode(module.getModuleCode());
                    dto.setModuleName(module.getModuleName());
                    
                    // Map status/grade to gpaStatus
                    String grade = registration.getGrade();
                    if ("G".equals(grade)) {
                        dto.setGpaStatus(GPAStatus.GPA);
                    } else if ("N".equals(grade)) {
                        dto.setGpaStatus(GPAStatus.NGPA);
                    } else {
                        // Default if not recognized
                        dto.setGpaStatus(GPAStatus.GPA);
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }
}