package com.ruh.mis.service;

import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.DTO.AssignmentMarksCreateDTO;
import com.ruh.mis.model.DTO.AssignmentMarksDTO;

import java.util.List;

public interface AssignmentService {

    List<AssignmentDTO> findAll();

    List<AssignmentMarksDTO> findAllMarks();

    AssignmentDTO findById(int theId);

    List<AssignmentDTO> getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(int departmentId, int intakeId, int semesterId, int moduleId);

    Assignment save(AssignmentCreateDTO theAssignmentCreateDTO);

    void saveAssignmentMarksList(List<AssignmentMarksCreateDTO> assignmentMarksCreateDTOList);

    void deleteById(int theId);

    AssignmentDTO update(int assignmentId, AssignmentCreateDTO assignmentCreateDTO);

}
