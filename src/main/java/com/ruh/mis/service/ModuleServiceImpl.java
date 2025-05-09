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

    // StudentRepository injected for potential future student-module relationship operations
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
    }

    @Override
    public void deleteById(int theId) {
        moduleRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public ModuleDTO update(int moduleId, ModuleCreateDTO moduleCreateDTO) {
        // Find the existing department
        Module existingModule = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found: " + moduleId));

        // Update the fields
        existingModule.setModuleName(moduleCreateDTO.getModuleName());
        existingModule.setModuleCode(moduleCreateDTO.getModuleCode());
        existingModule.setCredit(moduleCreateDTO.getCredit());
        existingModule.setGpaStatus(moduleCreateDTO.getGpaStatus());
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