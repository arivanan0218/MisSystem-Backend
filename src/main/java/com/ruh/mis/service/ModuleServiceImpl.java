package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.Module;
import com.ruh.mis.repository.ModuleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ModuleDTO> findAll() {
        return moduleRepository.findAll().stream()
                .map(module -> modelMapper.map(module, ModuleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ModuleRegistrationDTO> findAllRegistration() {
        return moduleRepository.findAll().stream()
                .map(registration -> modelMapper.map(registration, ModuleRegistrationDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public ModuleDTO findById(int theId) {
        Module module = moduleRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Module not found: " + theId));
        return modelMapper.map(module, ModuleDTO.class);
    }

    @Override
    public ModuleRegistrationDTO findByRegistrationId(int theRegistrationId) {
        Module registration = moduleRepository.findById(theRegistrationId)
                .orElseThrow(() -> new RuntimeException("Module Registration not found: " + theRegistrationId));
        return modelMapper.map(registration, ModuleRegistrationDTO.class);
    }


    @Override
    public List<ModuleDTO> getModuleByDepartmentIdAndIntakeIdAndSemesterId(int departmentId, int intakeId, int semesterId) {
        List<Module> modules = moduleRepository.findByDepartmentIdAndIntakeIdAndSemesterId(departmentId, intakeId, semesterId);

        return modules.stream()
                .map(module -> modelMapper.map(module,ModuleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ModuleRegistrationDTO> getModuleRegistrationByDepartmentIdAndIntakeIdAndSemesterId(int departmentId, int intakeId, int semesterId) {
        List<Module> modules = moduleRepository.findByDepartmentIdAndIntakeIdAndSemesterId(departmentId, intakeId, semesterId);

        return modules.stream()
                .map(module -> modelMapper.map(module,ModuleRegistrationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Module save(ModuleCreateDTO theModuleCreateDTO) {
        Module module = modelMapper.map(theModuleCreateDTO, Module.class);
        return moduleRepository.save(module);
    }

    @Override
    public Module save(ModuleRegistrationCreateDTO theModuleRegistrationCreateDTO) {
        Module module = modelMapper.map(theModuleRegistrationCreateDTO, Module.class);
        return moduleRepository.save(module);
    }

    @Override
    public void deleteById(int theId) {
        moduleRepository.deleteById(theId);
    }
}
