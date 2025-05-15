package com.ruh.mis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruh.mis.model.DTO.AdminRegistrationUpdateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;
import com.ruh.mis.model.DTO.TakenModuleDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.GPAStatus;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.ModuleRegistration;
import com.ruh.mis.model.ModuleType;
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
    @Transactional
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
        
        // Validate module selections
        validateModuleSelections(requestDTO.getTakenModules());
        
        for (TakenModuleDTO takenModule : requestDTO.getTakenModules()) {
            Module module = moduleRepository.findById(takenModule.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found: " + takenModule.getModuleId()));
            
            // Validate that module type matches selection rules
            validateModuleTypeAndGPAStatus(module.getModuleType(), takenModule.getGpaStatus());
            
            // Check if registration already exists
            Optional<ModuleRegistration> existingReg = moduleRegistrationRepository
                    .findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(
                            requestDTO.getStudentId(),
                            requestDTO.getSemesterId(),
                            requestDTO.getIntakeId(),
                            requestDTO.getDepartmentId(),
                            takenModule.getModuleId());
            
            if (existingReg.isPresent()) {
                // Update existing registration
                updateRegistration(existingReg.get(), takenModule.getGpaStatus());
            } else {
                // Create new registration
                createRegistration(student, semester, intake, department, module, takenModule.getGpaStatus());
            }
        }
    }

    private void validateModuleSelections(List<TakenModuleDTO> takenModules) {
        // Check for CM modules - all must be taken
        boolean allCoreTaken = takenModules.stream()
                .filter(module -> "CM".equals(module.getModuleType()))
                .allMatch(module -> !"N".equals(module.getGpaStatus()) && !"-".equals(module.getGpaStatus()));
        
        if (!allCoreTaken) {
            throw new RuntimeException("All Core Modules (CM) must be taken");
        }
        
        // Check GE modules - should always be NGPA
        boolean geModulesValid = takenModules.stream()
                .filter(module -> "GE".equals(module.getModuleType()) && !"-".equals(module.getGpaStatus()))
                .allMatch(module -> "N".equals(module.getGpaStatus()));
        
        if (!geModulesValid) {
            throw new RuntimeException("General Elective (GE) modules must be Non-GPA (N)");
        }
    }
    
    private void validateModuleTypeAndGPAStatus(ModuleType moduleType, String gpaStatus) {
        switch (moduleType) {
            case CM:
                // Core modules must be taken and must be GPA
                if (!gpaStatus.equals("G")) {
                    throw new RuntimeException("Core Modules must be taken as GPA");
                }
                break;
            case TE:
                // Technical electives can be GPA or NGPA if taken
                if (!gpaStatus.equals("G") && !gpaStatus.equals("N") && !gpaStatus.equals("-")) {
                    throw new RuntimeException("Technical Electives must be GPA, NGPA, or not taken");
                }
                break;
            case GE:
                // General electives must be NGPA if taken
                if (!gpaStatus.equals("N") && !gpaStatus.equals("-")) {
                    throw new RuntimeException("General Electives must be NGPA");
                }
                break;
            default:
                throw new RuntimeException("Invalid module type");
        }
    }
    
    private void updateRegistration(ModuleRegistration registration, String gpaStatus) {
        // Update based on GPA status code
        switch (gpaStatus) {
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
            default -> throw new RuntimeException("Invalid GPA Status: " + gpaStatus);
        }
        
        // Reset registration status to Pending for any updates
        registration.setRegistrationStatus("Pending");
        
        moduleRegistrationRepository.save(registration);
    }
    
    private void createRegistration(Student student, Semester semester, Intake intake, 
            Department department, Module module, String gpaStatus) {
        
        ModuleRegistration registration = new ModuleRegistration();
        registration.setStudent(student);
        registration.setSemester(semester);
        registration.setIntake(intake);
        registration.setDepartment(department);
        registration.setModule(module);
        
        // Set grade and status based on GPA status code
        switch (gpaStatus) {
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
            default -> throw new RuntimeException("Invalid GPA Status: " + gpaStatus);
        }
        
        // Set initial registration status to Pending
        registration.setRegistrationStatus("Pending");
        
        moduleRegistrationRepository.save(registration);
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
                    moduleMap.put("type", reg.getModule().getModuleType().toString());
                    moduleMap.put("registrationStatus", reg.getRegistrationStatus());
                    moduleMap.put("registrationId", String.valueOf(reg.getId()));
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
        return mapToModuleRegistrationDTOs(registrations);
    }
    
    @Override
    public List<ModuleRegistrationDTO> getPendingRegistrationsByDepartmentIntakeSemester(
            int departmentId, int intakeId, int semesterId) {
        
        List<ModuleRegistration> registrations = moduleRegistrationRepository
                .findByDepartmentIdAndIntakeIdAndSemesterIdAndRegistrationStatus(
                        departmentId, intakeId, semesterId, "Pending");
        
        return mapToModuleRegistrationDTOs(registrations);
    }
    
    @Override
    public List<ModuleRegistrationDTO> getPendingRegistrationsByModule(
            int moduleId, int semesterId, int intakeId, int departmentId) {
        
        List<ModuleRegistration> registrations = moduleRegistrationRepository
                .findByModuleIdAndSemesterIdAndIntakeIdAndDepartmentId(
                        moduleId, semesterId, intakeId, departmentId)
                .stream()
                .filter(reg -> "Pending".equals(reg.getRegistrationStatus()))
                .collect(Collectors.toList());
        
        return mapToModuleRegistrationDTOs(registrations);
    }
    
    @Override
    @Transactional
    public ModuleRegistrationDTO updateRegistrationStatus(AdminRegistrationUpdateDTO updateDTO) {
        ModuleRegistration registration = moduleRegistrationRepository.findById(updateDTO.getRegistrationId())
                .orElseThrow(() -> new RuntimeException("Registration not found: " + updateDTO.getRegistrationId()));
        
        switch (updateDTO.getAction()) {
            case "APPROVE":
                registration.setRegistrationStatus("Approved");
                break;
            case "REJECT":
                registration.setRegistrationStatus("Rejected");
                break;
            case "EDIT":
                // Update GPA status if provided
                if (updateDTO.getGpaStatus() != null) {
                    // Validate the change based on module type
                    validateModuleTypeAndGPAStatus(
                            registration.getModule().getModuleType(), 
                            updateDTO.getGpaStatus().toString());
                    
                    switch (updateDTO.getGpaStatus()) {
                        case GPA:
                            registration.setGrade("G");
                            break;
                        case NGPA:
                            registration.setGrade("N");
                            break;
                    }
                }
                
                // Update taken status if provided
                if (updateDTO.getStatus() != null) {
                    registration.setStatus(updateDTO.getStatus());
                    
                    // If changing to Not-Taken, update grade accordingly
                    if ("Not-Taken".equals(updateDTO.getStatus())) {
                        registration.setGrade("-");
                    }
                }
                
                registration.setRegistrationStatus("Pending"); // Reset to pending after edit
                break;
            default:
                throw new RuntimeException("Invalid action: " + updateDTO.getAction());
        }
        
        ModuleRegistration savedRegistration = moduleRegistrationRepository.save(registration);
        return mapToModuleRegistrationDTO(savedRegistration);
    }
    
    private List<ModuleRegistrationDTO> mapToModuleRegistrationDTOs(List<ModuleRegistration> registrations) {
        return registrations.stream()
                .map(this::mapToModuleRegistrationDTO)
                .collect(Collectors.toList());
    }
    
    private ModuleRegistrationDTO mapToModuleRegistrationDTO(ModuleRegistration registration) {
        ModuleRegistrationDTO dto = new ModuleRegistrationDTO();
        dto.setId(registration.getId());
        dto.setSemesterId(registration.getSemester().getId());
        dto.setIntakeId(registration.getIntake().getId());
        dto.setDepartmentId(registration.getDepartment().getId());
        
        // Student information
        Student student = registration.getStudent();
        dto.setStudentId(student.getId());
        dto.setStudentName(student.getStudentName());
        dto.setStudentRegNo(student.getStudentRegNo());
        
        // Module information
        Module module = registration.getModule();
        dto.setModuleId(module.getId());
        dto.setModuleCode(module.getModuleCode());
        dto.setModuleName(module.getModuleName());
        dto.setModuleType(module.getModuleType());
        
        // Registration information
        dto.setStatus(registration.getStatus());
        dto.setRegistrationStatus(registration.getRegistrationStatus());
        
        // Map grade to gpaStatus
        String grade = registration.getGrade();
        if ("G".equals(grade)) {
            dto.setGpaStatus(GPAStatus.GPA);
        } else if ("N".equals(grade)) {
            dto.setGpaStatus(GPAStatus.NGPA);
        } else {
            // Default if not recognized
            dto.setGpaStatus(null);
        }
        
        return dto;
    }
}