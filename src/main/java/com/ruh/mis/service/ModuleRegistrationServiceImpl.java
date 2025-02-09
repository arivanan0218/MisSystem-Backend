package com.ruh.mis.service;

import com.ruh.mis.model.*;
import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;
import com.ruh.mis.model.Module;
import com.ruh.mis.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ModuleRegistrationServiceImpl implements ModuleRegistrationService {

    @Autowired
    private ModuleRegistrationRepository registrationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void registerModules(ModuleRegistrationRequestDTO requestDTO) {
        Student student = studentRepository.findById(requestDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));


        for (int moduleId : requestDTO.getTakenModuleIds()) {
            Module module = moduleRepository.findById(moduleId)
                    .orElseThrow(() -> new RuntimeException("Module not found"));

            ModuleRegistration registration = new ModuleRegistration();
            registration.setStudent(student);
            registration.setModule(module);
            registration.setStatus("Taken");

            // Set the grade based on the module type
            if ("GPA".equals(module.getGPA_Status())) {
                registration.setGrade("G");
            } else if ("NGPA".equals(module.getGPA_Status())) {
                registration.setGrade("N");
            }

            registrationRepository.save(registration);
        }

    }

    @Override
    public ModuleRegistrationResponseDTO getRegistrationDetailsForStudent(int studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<ModuleRegistration> registrations = registrationRepository.findByStudentId(studentId);

        ModuleRegistrationResponseDTO response = new ModuleRegistrationResponseDTO();
        response.setId(student.getId());
        response.setStudentName(student.getStudent_name());
        response.setStudentRegNo(student.getStudent_Reg_No());
        response.setDepartmentName(student.getDepartment().getDepartmentName());

        // Create a list to hold module details with attributes
        List<Map<String, String>> moduleDetails = new ArrayList<>();
        for (ModuleRegistration registration : registrations) {
            Map<String, String> details = new HashMap<>();
            details.put("moduleId", String.valueOf(registration.getModule().getId()));
            details.put("moduleCode", registration.getModule().getModuleCode());
            details.put("moduleName", registration.getModule().getModuleName());
            details.put("status", registration.getStatus());
            details.put("grade", registration.getGrade());
            moduleDetails.add(details);
        }

        // Set module details in the response DTO
        response.setModules(moduleDetails);
        return response;
    }
}
