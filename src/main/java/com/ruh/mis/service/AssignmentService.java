package com.ruh.mis.service;

import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.Assignment;

import java.util.List;

public interface AssignmentService {

    List<AssignmentDTO> findAll();

    List<MarksDTO> findAllMarks();

    AssignmentDTO findById(int theId);

    List<AssignmentDTO> getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(int departmentId, int intakeId, int semesterId, int moduleId);

    Assignment save(AssignmentCreateDTO theAssignmentCreateDTO);

    void saveMarksList(List<MarksCreateDTO> marksCreateDTOList); // ✅ Fixed method name

    void deleteById(int theId);

    AssignmentDTO update(int assignmentId, AssignmentCreateDTO assignmentCreateDTO);
}
