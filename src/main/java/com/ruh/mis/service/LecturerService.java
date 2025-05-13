package com.ruh.mis.service;

import java.util.List;

import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.LecturerDTO;
import com.ruh.mis.model.Lecturer;

public interface LecturerService {
    List<LecturerDTO> findAll();

    LecturerDTO findById(int theId);

    Lecturer save(LecturerCreateDTO theLecturerCreateDTO);

    void saveLecturersList(List<LecturerCreateDTO> lecturerCreateDTOList);

    void deleteById(int theId);

    List<LecturerDTO> findByDepartmentId(int departmentId);
}