package com.ruh.mis.service;

import com.ruh.mis.model.DTO.FinalResultsDTO;

import java.util.List;

public interface FinalResultsService {
    
    /**
     * Calculate final results for all students in a department and intake
     * 
     * @param departmentId The ID of the department
     * @param intakeId The ID of the intake
     * @return true if calculation is successful, false otherwise
     */
    boolean calculateFinalResults(int departmentId, int intakeId);
    
    /**
     * Get final results for all students in a department and intake
     * 
     * @param departmentId The ID of the department
     * @param intakeId The ID of the intake
     * @return List of FinalResultsDTO objects
     */
    List<FinalResultsDTO> getFinalResults(int departmentId, int intakeId);
    
    /**
     * Get final results for a specific student
     * 
     * @param departmentId The ID of the department
     * @param intakeId The ID of the intake
     * @param studentId The ID of the student
     * @return FinalResultsDTO object if found, null otherwise
     */
    FinalResultsDTO getFinalResultsByStudent(int departmentId, int intakeId, int studentId);
}
