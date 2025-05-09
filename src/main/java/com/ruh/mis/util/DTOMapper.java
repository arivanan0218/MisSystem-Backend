package com.ruh.mis.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.ruh.mis.model.DTO.ModuleResultDTO;
import com.ruh.mis.model.DTO.StudentDTO;
import com.ruh.mis.model.ModuleResult;
import com.ruh.mis.model.Student;

/**
 * Utility class for handling entity-to-DTO mapping with proper ID handling
 */
@Component
public class DTOMapper {
    
    private final ModelMapper modelMapper;
    
    public DTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    /**
     * Maps a Student entity to StudentDTO with proper ID handling
     */
    public StudentDTO mapStudentToDTO(Student student) {
        if (student == null) {
            return null;
        }
        
        StudentDTO dto = modelMapper.map(student, StudentDTO.class);
        
        // Handle entity references using accessor methods
        if (student.getDepartment() != null) {
            dto.setDepartmentId(student.getDepartment().getId());
        }
        
        if (student.getIntake() != null) {
            dto.setIntakeId(student.getIntake().getId());
        }
        
        return dto;
    }
    
    /**
     * Maps a ModuleResult entity to ModuleResultDTO with proper ID handling
     */
    public ModuleResultDTO mapModuleResultToDTO(ModuleResult result) {
        if (result == null) {
            return null;
        }
        
        ModuleResultDTO dto = modelMapper.map(result, ModuleResultDTO.class);
        
        // Handle entity references
        if (result.getDepartment() != null) {
            dto.setDepartmentId(result.getDepartment().getId());
        }
        
        if (result.getIntake() != null) {
            dto.setIntakeId(result.getIntake().getId());
        }
        
        if (result.getSemester() != null) {
            dto.setSemesterId(result.getSemester().getId());
        }
        
        if (result.getModule() != null) {
            dto.setModuleId(result.getModule().getId());
        }
        
        if (result.getStudent() != null) {
            dto.setStudentId(result.getStudent().getId());
        }
        
        return dto;
    }
}
