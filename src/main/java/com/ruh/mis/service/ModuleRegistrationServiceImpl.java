package com.ruh.mis.service;

import com.ruh.mis.model.*;
import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationDTO;
import com.ruh.mis.model.Module;
import com.ruh.mis.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuleRegistrationServiceImpl implements ModuleRegistrationService {

    @Autowired
    private ModuleRegistrationRepository moduleRegistrationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ModuleRegistrationDTO> findAll() {
        return moduleRegistrationRepository.findAll().stream()
                .map(moduleRegistration -> {
                    ModuleRegistrationDTO moduleRegistrationDTO = modelMapper.map(moduleRegistration, ModuleRegistrationDTO.class);

                    if (!moduleRegistration.getStudents().isEmpty()) {
                        Student student = moduleRegistration.getStudents().get(0);
                        moduleRegistrationDTO.setStudentName(student.getStudent_name());
                        moduleRegistrationDTO.setStudentReg(student.getStudent_Reg_No());
                    }

                    if(!moduleRegistration.getModules().isEmpty()) {
                        Module module = moduleRegistration.getModules().get(0);
                        moduleRegistrationDTO.setModuleCode(module.getModuleCode());
                        moduleRegistrationDTO.setModuleName(module.getModuleName());
                    }

                    return moduleRegistrationDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ModuleRegistrationDTO> getModuleRegistrationByStudentId(int studentId) {
        List<ModuleRegistration> moduleRegistrations = moduleRegistrationRepository.findBYStudentId(studentId);

        return moduleRegistrations.stream()
                .map(moduleRegistration -> {
                    ModuleRegistrationDTO moduleRegistrationDTO = modelMapper.map(moduleRegistration, ModuleRegistrationDTO.class);

                    // Set student details if available
                    if (!moduleRegistration.getStudents().isEmpty()) {
                        Student student = moduleRegistration.getStudents().get(0);
                        moduleRegistrationDTO.setStudentName(student.getStudent_name());
                        moduleRegistrationDTO.setStudentReg(student.getStudent_Reg_No());
                    }

                    // Set module details if available
                    if (!moduleRegistration.getModules().isEmpty()) {
                        Module module = moduleRegistration.getModules().get(0);
                        moduleRegistrationDTO.setModuleCode(module.getModuleCode());
                        moduleRegistrationDTO.setModuleName(module.getModuleName());
                    }

                    return moduleRegistrationDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void saveModuleRegistrationList(List<ModuleRegistrationCreateDTO> moduleRegistrationCreateDTOList) {
        for (ModuleRegistrationCreateDTO createDTO : moduleRegistrationCreateDTOList) {
            ModuleRegistration moduleRegistration = modelMapper.map(createDTO, ModuleRegistration.class);

            // Retrieve and set the department
            Optional<Department> departmentOpt = departmentRepository.findById(createDTO.getDepartmentId());
            if (departmentOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid department ID: " + createDTO.getDepartmentId());
            }
            moduleRegistration.setDepartment(departmentOpt.get());

            // Retrieve and set the intake
            Optional<Intake> intakeOpt = intakeRepository.findById(createDTO.getIntakeId());
            if (intakeOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid intake ID: " + createDTO.getIntakeId());
            }
            moduleRegistration.setIntake(intakeOpt.get());

            // Retrieve and set the semester
            Optional<Semester> semesterOpt = semesterRepository.findById(createDTO.getSemesterId());
            if (semesterOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid semester ID: " + createDTO.getSemesterId());
            }
            moduleRegistration.setSemester(semesterOpt.get());

            // Retrieve and set the student
            Optional<Student> studentOpt = studentRepository.findById(createDTO.getStudentId());
            if (studentOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid student ID: " + createDTO.getStudentId());
            }
            moduleRegistration.setStudents(List.of(studentOpt.get()));

            // Retrieve and set the module
            Optional<Module> moduleOpt = moduleRepository.findById(createDTO.getModuleId());
            if (moduleOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid module ID: " + createDTO.getModuleId());
            }
            moduleRegistration.setModules(List.of(moduleOpt.get()));

            // Save module registration
            moduleRegistrationRepository.save(moduleRegistration);
        }
    }
}
