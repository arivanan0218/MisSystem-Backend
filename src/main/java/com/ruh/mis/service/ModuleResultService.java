package com.ruh.mis.service;

import java.util.List;

import com.ruh.mis.model.DTO.ModuleResultDTO;

public interface ModuleResultService {
    /**
     * Calculate module results for all students in a given module
     * This includes weighted assignment marks and pass/fail status based on 35% threshold
     */
    void calculateModuleResults(int departmentId, int intakeId, int semesterId, int moduleId);
    
    /**
     * Get results for a specific module for a specific student
     * Includes detailed assignment breakdown with weighted marks
     */
    List<ModuleResultDTO> getModuleResults(int departmentId, int intakeId, int semesterId, int moduleId, int studentId);
    
    /**
     * Get results for all students for a specific module
     * Includes detailed assignment breakdown with weighted marks
     */
    List<ModuleResultDTO> getModuleResults(int departmentId, int intakeId, int semesterId, int moduleId);
    
    /**
     * Get all module results for a specific student in a specific semester
     * Includes detailed assignment breakdown with weighted marks
     */
    List<ModuleResultDTO> getSemesterStudentResults(int departmentId, int intakeId, int semesterId, int studentId);
    
    /**
     * Get all module results for a student with optional filters
     * Includes detailed assignment breakdown with weighted marks
     */
    List<ModuleResultDTO> getStudentModuleResults(int studentId, Integer departmentId, Integer intakeId, Integer semesterId, Integer moduleId);
    
    /**
     * Update the status of a module result (PASS/FAIL)
     * @param resultId The ID of the module result to update
     * @param newStatus The new status to set (should be "PASS" or "FAIL")
     * @return true if update was successful, false otherwise
     */
    boolean updateModuleResultStatus(int resultId, String newStatus);
}