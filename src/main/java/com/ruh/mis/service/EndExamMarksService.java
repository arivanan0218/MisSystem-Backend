package com.ruh.mis.service;

import com.ruh.mis.model.DTO.EndExamMarksCreateDTO;
import com.ruh.mis.model.DTO.EndExamMarksDTO;

import java.util.List;

public interface EndExamMarksService {
    void saveEndExamMarksList(List<EndExamMarksCreateDTO> marksList);
    double calculateFinalModuleMarks(int studentId, int moduleId);
    List<EndExamMarksDTO> getEndExamMarksForStudent(int studentId);
    EndExamMarksDTO updateEndExamMarks(EndExamMarksDTO marksDTO);
    void deleteEndExamMarksById(int id);
}