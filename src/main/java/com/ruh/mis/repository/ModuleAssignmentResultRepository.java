package com.ruh.mis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ruh.mis.model.ModuleAssignmentResult;
import com.ruh.mis.model.ModuleResult;

public interface ModuleAssignmentResultRepository extends JpaRepository<ModuleAssignmentResult, Integer> {
    
    List<ModuleAssignmentResult> findByModuleResultId(int moduleResultId);
    
    void deleteByModuleResultId(int moduleResultId);
    
    List<ModuleAssignmentResult> findByModuleResult(ModuleResult moduleResult);
}
