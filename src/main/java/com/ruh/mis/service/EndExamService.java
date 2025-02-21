package com.ruh.mis.service;

import com.ruh.mis.model.DTO.EndExamCreateDTO;
import com.ruh.mis.model.DTO.EndExamDTO;
import com.ruh.mis.model.EndExam;
import java.util.List;

public interface EndExamService {
    List<EndExamDTO> findAll();
    EndExamDTO findById(int theId);
    List<EndExamDTO> getEndExamByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
            int departmentId, int intakeId, int semesterId, int moduleId);
    EndExamDTO save(EndExamCreateDTO endExamCreateDTO);
    void deleteById(int theId);
    EndExamDTO update(int endExamId, EndExamCreateDTO endExamCreateDTO);
}