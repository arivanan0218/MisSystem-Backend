package com.ruh.mis.cofig;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.IntakeCreateDTO;
import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.LecturerDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.ModuleCreateDTO;
import com.ruh.mis.model.DTO.ModuleRegistrationCreateDTO;
import com.ruh.mis.model.DTO.ResultsCreateDTO;
import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.StudentCreateDTO;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Lecturer;
import com.ruh.mis.model.Marks;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.ModuleRegistration;
import com.ruh.mis.model.Results;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Student;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configure the model mapper to be more strict and skip null values
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true)
            .setAmbiguityIgnored(true)  // Important: Ignore ambiguity in mappings
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Custom mapping for Lecturer to LecturerDTO - handling the department relation
        modelMapper.addMappings(new PropertyMap<Lecturer, LecturerDTO>() {
            @Override
            protected void configure() {
                map(source.getDepartment().getId(), destination.getDepartmentId());
            }
        });

        // Custom mapping for LecturerCreateDTO to Lecturer - handling the department relation
        modelMapper.typeMap(LecturerCreateDTO.class, Lecturer.class)
            .addMappings(mapper -> {
                mapper.skip(Lecturer::setDepartment);
            });

        // Simple mappings for Create DTOs - just skip the ID field
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
        
        // Configure the model mapper for best general-purpose behavior
        modelMapper.getConfiguration()
            .setSkipNullEnabled(true)
            .setFieldMatchingEnabled(true);

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

        modelMapper.addMappings(new PropertyMap<MarksCreateDTO, Marks>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        
        return modelMapper;
    }
}