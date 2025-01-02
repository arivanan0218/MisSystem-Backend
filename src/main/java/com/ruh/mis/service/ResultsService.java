package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ResultsCreateDTO;
import com.ruh.mis.model.DTO.ResultsDTO;
import com.ruh.mis.model.Results;

import java.util.List;

public interface ResultsService {
    List<ResultsDTO> findAll();

    ResultsDTO findById(int theId);

    Results save(ResultsCreateDTO theResultsCreateDTO);

    void deleteById(int theId);
}
