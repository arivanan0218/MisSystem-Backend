package com.ruh.mis.cofig;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.IntakeCreateDTO;
import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ModuleResultDTO;
import com.ruh.mis.model.DTO.ResultsCreateDTO;
import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Lecturer;
import com.ruh.mis.model.Marks;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.ModuleRegistration;
import com.ruh.mis.model.ModuleResult;
import com.ruh.mis.model.Results;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Student;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<IntakeCreateDTO, Intake>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<LecturerCreateDTO, Lecturer>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<StudentCreateDTO, Student>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<SemesterCreateDTO, Semester>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<ModuleCreateDTO, Module>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<ModuleRegistrationCreateDTO, ModuleRegistration>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<AssignmentCreateDTO, Assignment>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<ResultsCreateDTO, Results>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<MarksCreateDTO, Marks>() { // ✅ Updated mapping
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<ModuleResult, ModuleResultDTO>() {
            @Override
            protected void configure() {
                // Map IDs from relationships
                map().setDepartmentId(source.getDepartment().getId());
                map().setIntakeId(source.getIntake().getId());
                map().setSemesterId(source.getSemester().getId());
                map().setModuleId(source.getModule().getId());
                map().setStudentId(source.getStudent().getId());

                // Map names from relationships
                map().setDepartmentName(source.getDepartment().getDepartmentName());
                map().setIntakeName(source.getIntake().getIntakeYear());
                map().setSemesterName(source.getSemester().getSemesterName());
                map().setModuleName(source.getModule().getModuleName());
                map().setStudentName(source.getStudent().getStudent_name());
            }
        });




        return modelMapper;
    }
}