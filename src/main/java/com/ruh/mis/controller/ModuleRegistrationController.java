package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;
import com.ruh.mis.service.ModuleRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module-registrations")
public class ModuleRegistrationController {

//    @Autowired
//    private ModuleRegistrationService moduleRegistrationService;
//
//    @GetMapping("/")
//    public ResponseEntity<List<ModuleRegistrationDTO>> getAllModuleRegistrations() {
//        List<ModuleRegistrationDTO> moduleRegistrations = moduleRegistrationService.findAll();
//        return ResponseEntity.ok(moduleRegistrations);
//    }
//
//    @GetMapping("module-registration/{studentId}")
//    public List<ModuleRegistrationDTO> getModuleRegistrationByStudentId(@PathVariable int studentId) {
//        return moduleRegistrationService.getModuleRegistrationByStudentId(studentId);
//    }
//
//    @PostMapping("/create-list")
//    public ResponseEntity<Void> createModuleRegistrations(@RequestBody List<ModuleRegistrationCreateDTO> moduleRegistrationCreateDTOList) {
//        moduleRegistrationService.saveModuleRegistrationList(moduleRegistrationCreateDTOList);
//        return ResponseEntity.ok().build();
//    }

    @Autowired
    private ModuleRegistrationService registrationService;

    @PostMapping
    public ResponseEntity<String> registerModules(@RequestBody ModuleRegistrationRequestDTO requestDTO) {
        registrationService.registerModules(requestDTO);
        return ResponseEntity.ok("Modules registered successfully!");
    }

    @GetMapping("/student/{studentId}/semester/{semesterId}/intake/{intakeId}/department/{departmentId}")
    public ResponseEntity<ModuleRegistrationResponseDTO> getRegistrationsForStudent(
            @PathVariable int studentId,
            @PathVariable int semesterId,
            @PathVariable int intakeId,
            @PathVariable int departmentId) {
        return ResponseEntity.ok(registrationService.getRegistrationDetailsForStudent(studentId, semesterId, intakeId, departmentId));
    }
}
