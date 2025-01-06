package com.ruh.mis.service;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.Marks;

import java.util.List;

public interface MarksService {
    List<MarksDTO> findAll();

    MarksDTO findById(int theId);

    List<MarksDTO> getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleAndAssignmentId(int departmentId, int intakeId, int semesterId, int moduleId, int assignmentId);

    Marks save(MarksCreateDTO theMarksCreateDTO);

    void saveMarksList(List<MarksCreateDTO> marksCreateDTOList);

    void deleteById(int theId);
}
