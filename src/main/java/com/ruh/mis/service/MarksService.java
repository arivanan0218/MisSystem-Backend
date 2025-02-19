package com.ruh.mis.service;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.DTO.MarksResponseDTO;

import java.util.List;

public interface MarksService {
    List<MarksDTO> findAll();
    MarksDTO findById(int id);
    void saveMarksList(List<MarksCreateDTO> marksCreateDTOList);
    MarksResponseDTO getMarksForStudent(int studentId);
    void deleteById(int id);
}
