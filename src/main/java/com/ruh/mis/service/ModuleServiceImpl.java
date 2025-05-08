package com.ruh.mis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.model.Module;
import com.ruh.mis.repository.ModuleRepository;
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
    public Module save(ModuleCreateDTO theModuleCreateDTO) {
        Module module = modelMapper.map(theModuleCreateDTO, Module.class);
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
