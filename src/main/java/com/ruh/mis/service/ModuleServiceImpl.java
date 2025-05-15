package com.ruh.mis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.GPAStatus;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.Semester;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.IntakeRepository;
import com.ruh.mis.repository.ModuleRepository;
import com.ruh.mis.repository.SemesterRepository;
import com.ruh.mis.repository.StudentRepository;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @SuppressWarnings("unused")
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ModuleDTO> findAll() {
        return moduleRepository.findAll().stream()
                .map(module -> {
                    ModuleDTO dto = modelMapper.map(module, ModuleDTO.class);

                    // Explicitly set entity IDs
                    if (module.getDepartment() != null) {
                        dto.setDepartmentId(module.getDepartment().getId());
                    }

                    if (module.getIntake() != null) {
                        dto.setIntakeId(module.getIntake().getId());
                    }

                    if (module.getSemester() != null) {
                        dto.setSemesterId(module.getSemester().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ModuleDTO findById(int theId) {
        Module module = moduleRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Module not found: " + theId));

        ModuleDTO dto = modelMapper.map(module, ModuleDTO.class);

        // Explicitly set entity IDs
        if (module.getDepartment() != null) {
            dto.setDepartmentId(module.getDepartment().getId());
        }

        if (module.getIntake() != null) {
            dto.setIntakeId(module.getIntake().getId());
        }

        if (module.getSemester() != null) {
            dto.setSemesterId(module.getSemester().getId());
        }

        return dto;
    }

    @Override
    public List<ModuleDTO> getModuleByDepartmentIdAndIntakeIdAndSemesterId(int departmentId, int intakeId, int semesterId) {
        List<Module> modules = moduleRepository.findByDepartmentIdAndIntakeIdAndSemesterId(departmentId, intakeId, semesterId);

        return modules.stream()
                .map(module -> {
                    ModuleDTO dto = modelMapper.map(module, ModuleDTO.class);

                    // Explicitly set entity IDs
                    if (module.getDepartment() != null) {
                        dto.setDepartmentId(module.getDepartment().getId());
                    }

                    if (module.getIntake() != null) {
                        dto.setIntakeId(module.getIntake().getId());
                    }

                    if (module.getSemester() != null) {
                        dto.setSemesterId(module.getSemester().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Module save(ModuleCreateDTO theModuleCreateDTO) {
        try {
            // Validate and adjust data based on module type
            validateAndAdjustByModuleType(theModuleCreateDTO);
            
            // Create a new Module entity
            Module module = modelMapper.map(theModuleCreateDTO, Module.class);

            // Fetch the required entities from repositories using their IDs from the DTO
            Department department = departmentRepository.findById(theModuleCreateDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found: " + theModuleCreateDTO.getDepartmentId()));

            Intake intake = intakeRepository.findById(theModuleCreateDTO.getIntakeId())
                    .orElseThrow(() -> new RuntimeException("Intake not found: " + theModuleCreateDTO.getIntakeId()));

            Semester semester = semesterRepository.findById(theModuleCreateDTO.getSemesterId())
                    .orElseThrow(() -> new RuntimeException("Semester not found: " + theModuleCreateDTO.getSemesterId()));

            // Set the relationships manually
            module.setDepartment(department);
            module.setIntake(intake);
            module.setSemester(semester);

            // Save the module
            return moduleRepository.save(module);
        } catch (Exception e) {
            // Log the detailed error for debugging
            System.err.println("Error creating module: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create module: " + e.getMessage(), e);
        }
    }

    private void validateAndAdjustByModuleType(ModuleCreateDTO moduleCreateDTO) {
        if (moduleCreateDTO.getModuleType() == null) {
            throw new RuntimeException("Module type is required");
        }

        // Apply business rules based on module type
        switch (moduleCreateDTO.getModuleType()) {
            case CM:
                // Core Modules are always GPA
                moduleCreateDTO.setGpaStatus(GPAStatus.GPA);
                
                // Ensure credit is provided
                if (moduleCreateDTO.getCredit() == null || moduleCreateDTO.getCredit() <= 0) {
                    throw new RuntimeException("Credit value is required for Core Modules");
                }
                break;
                
            case TE:
                // Technical Electives can be either GPA or Non-GPA (set by user)
                // No restrictions here, as user selection is valid
                // Ensure credit is provided
                if (moduleCreateDTO.getCredit() == null || moduleCreateDTO.getCredit() <= 0) {
                    throw new RuntimeException("Credit value is required for Technical Electives");
                }
                break;
                
            case GE:
                // General Electives are always Non-GPA
                moduleCreateDTO.setGpaStatus(GPAStatus.NGPA);
                
                // General Electives don't require credits
                moduleCreateDTO.setCredit(0);
                break;
                
            default:
                throw new RuntimeException("Invalid module type");
        }
    }

    @Override
    public void deleteById(int theId) {
        moduleRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public ModuleDTO update(int moduleId, ModuleCreateDTO moduleCreateDTO) {
        // Find the existing module
        Module existingModule = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found: " + moduleId));

        // Validate and adjust data based on module type
        validateAndAdjustByModuleType(moduleCreateDTO);
        
        // Update the fields
        existingModule.setModuleName(moduleCreateDTO.getModuleName());
        existingModule.setModuleCode(moduleCreateDTO.getModuleCode());
        existingModule.setCredit(moduleCreateDTO.getCredit());
        existingModule.setGpaStatus(moduleCreateDTO.getGpaStatus());
        existingModule.setModuleType(moduleCreateDTO.getModuleType());
        existingModule.setModuleCoordinator(moduleCreateDTO.getModuleCoordinator());

        // Save the updated entity
        Module updatedModule = moduleRepository.save(existingModule);

        // Map the updated entity to DTO and return with explicit ID setting
        ModuleDTO dto = modelMapper.map(updatedModule, ModuleDTO.class);

        // Explicitly set entity IDs
        if (updatedModule.getDepartment() != null) {
            dto.setDepartmentId(updatedModule.getDepartment().getId());
        }

        if (updatedModule.getIntake() != null) {
            dto.setIntakeId(updatedModule.getIntake().getId());
        }

        if (updatedModule.getSemester() != null) {
            dto.setSemesterId(updatedModule.getSemester().getId());
        }

        return dto;
    }
}