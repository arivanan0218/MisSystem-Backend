package com.ruh.mis.service;

import java.util.List;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.DTO.MarksResponseDTO;

public interface MarksService {
    List<MarksDTO> findAll();
    MarksDTO findById(int id);
    void saveMarksList(List<MarksCreateDTO> marksCreateDTOList);
    MarksResponseDTO getMarksForStudent(int studentId);
    MarksDTO updateMarks(int id, MarksCreateDTO marksCreateDTO);
    void deleteById(int id);
    List<MarksDTO> findByAssignmentId(int assignmentId); // New method to get marks by assignment ID
}