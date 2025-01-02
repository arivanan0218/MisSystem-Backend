package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleDTO;
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
    public ModuleDTO findById(int theId) {
        Module module = moduleRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Module not found: " + theId));
        return modelMapper.map(module, ModuleDTO.class);
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
}
