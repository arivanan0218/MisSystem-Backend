package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.model.Module;
import com.ruh.mis.repository.ModuleRepository;
import com.ruh.mis.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ModuleDTO> findAll() {
        return moduleRepository.findAll().stream()
                .map(module -> modelMapper.map(module, ModuleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ModuleDTO findById(int theId) {
        Module module = moduleRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Module not found: " + theId));
        return modelMapper.map(module, ModuleDTO.class);
    }


    @Override
    public List<ModuleDTO> getModuleByDepartmentIdAndIntakeIdAndSemesterId(int departmentId, int intakeId, int semesterId) {
        List<Module> modules = moduleRepository.findByDepartmentIdAndIntakeIdAndSemesterId(departmentId, intakeId, semesterId);

        return modules.stream()
                .map(module -> modelMapper.map(module,ModuleDTO.class))
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
        existingModule.setGPA_Status(moduleCreateDTO.getGPA_Status());
        existingModule.setModuleCoordinator(moduleCreateDTO.getModuleCoordinator());


        // Save the updated entity
        Module updatedModule = moduleRepository.save(existingModule);

        // Map the updated entity to DTO and return
        return modelMapper.map(updatedModule, ModuleDTO.class);
    }
}
