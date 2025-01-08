package com.ruh.mis.service;

import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.LecturerDTO;
import com.ruh.mis.model.Lecturer;

import java.util.List;

public interface LecturerService {
    List<LecturerDTO> findAll();

    LecturerDTO findById(int theId);

    Lecturer save(LecturerCreateDTO theLecturerCreateDTO);

    void saveLecturersList(List<LecturerCreateDTO> lecturerCreateDTOList);

    void deleteById(int theId);
}
