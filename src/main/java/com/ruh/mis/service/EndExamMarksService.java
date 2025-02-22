package com.ruh.mis.service;

import com.ruh.mis.model.DTO.EndExamMarksCreateDTO;
import com.ruh.mis.model.DTO.EndExamMarksDTO;
import com.ruh.mis.model.DTO.EndExamMarksResponseDTO;
import com.ruh.mis.model.DTO.ModuleMarksResponseDTO;

import java.util.List;

public interface EndExamMarksService {
    List<EndExamMarksDTO> findAll();

    EndExamMarksDTO findById(int id);

    EndExamMarksResponseDTO getFinalEndExamMarksForStudent(int studentId);

    void saveEndExamMarksList(List<EndExamMarksCreateDTO> marksList);
    ModuleMarksResponseDTO calculateFinalModuleMarks(int studentId, int moduleId);
    List<EndExamMarksDTO> getEndExamMarksForStudent(int studentId);
    EndExamMarksDTO updateEndExamMarks(EndExamMarksDTO marksDTO);
    void deleteEndExamMarksById(int id);
}