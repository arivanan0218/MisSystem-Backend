package com.ruh.mis.service;

import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.SemesterDTO;
import com.ruh.mis.model.Semester;

import java.util.List;

public interface SemesterService {
    List<SemesterDTO> findAll();

    SemesterDTO findById(int theId);

    Semester save(SemesterCreateDTO theSemesterCreateDTO);

    void deleteById(int theId);
}
