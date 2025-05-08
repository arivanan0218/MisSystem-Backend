package com.ruh.mis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruh.mis.model.DTO.ModuleRegistrationRequestDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationResponseDTO;
import com.ruh.mis.model.DTO.TakenModuleDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Module;
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

    private final ModuleRegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final DepartmentRepository departmentRepository;
    private final IntakeRepository intakeRepository;
    private final ModuleRepository moduleRepository;
    // ModelMapper is injected for consistency with other services and potential future DTO mapping needs
    @SuppressWarnings("unused")
    private final ModelMapper modelMapper;

    @Autowired
    public ModuleRegistrationServiceImpl(
            ModuleRegistrationRepository registrationRepository,
            StudentRepository studentRepository,
            SemesterRepository semesterRepository,
            DepartmentRepository departmentRepository,
            IntakeRepository intakeRepository,
            ModuleRepository moduleRepository,
            ModelMapper modelMapper) {
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.semesterRepository = semesterRepository;
        this.departmentRepository = departmentRepository;
        this.intakeRepository = intakeRepository;
        this.moduleRepository = moduleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerModules(ModuleRegistrationRequestDTO requestDTO) {
        Student student = studentRepository.findById(requestDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Semester semester = semesterRepository.findById(requestDTO.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        Intake intake = intakeRepository.findById(requestDTO.getIntakeId())
                .orElseThrow(() -> new RuntimeException("Intake not found"));

        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        for (TakenModuleDTO takenModule : requestDTO.getTakenModules()) {
            Module module = moduleRepository.findById(takenModule.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found"));

            // Check if the module is already registered
            Optional<ModuleRegistration> optionalRegistration = registrationRepository
                    .findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(
                            student.getId(), semester.getId(), intake.getId(), department.getId(), module.getId());

            ModuleRegistration registration = optionalRegistration.orElseGet(ModuleRegistration::new);

            // Set values if new registration
            if (!optionalRegistration.isPresent()) {
                registration.setStudent(student);
                registration.setSemester(semester);
                registration.setIntake(intake);
                registration.setDepartment(department);
                registration.setModule(module);
            }

            // Update grade and status using rule switch
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

            // Save registration
            registrationRepository.save(registration);
        }
    }

    @Override
    public ModuleRegistrationResponseDTO getRegistrationDetailsForStudent(int studentId, int semesterId, int intakeId, int departmentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<ModuleRegistration> registrations = registrationRepository
                .findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentId(studentId, semesterId, intakeId, departmentId);

        // Prepare response DTO
        ModuleRegistrationResponseDTO response = new ModuleRegistrationResponseDTO();
        response.setId(student.getId());
        response.setStudentName(student.getStudentName());
        response.setStudentRegNo(student.getStudentRegNo());
        response.setDepartmentName(student.getDepartment().getDepartmentName());

        // Populate module details
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

        response.setModules(moduleDetails);
        return response;
    }
}
