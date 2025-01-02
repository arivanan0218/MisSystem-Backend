package com.ruh.mis.cofig;

import com.ruh.mis.model.*;
import com.ruh.mis.model.DTO.*;
import com.ruh.mis.model.Module;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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


        modelMapper.addMappings(new PropertyMap<AssignmentCreateDTO, Assignment>() {
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

        modelMapper.addMappings(new PropertyMap<ResultsCreateDTO, Results>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        return modelMapper;
    }
}