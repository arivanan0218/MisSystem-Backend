package com.ruh.mis.service;

import com.ruh.mis.model.DTO.ResultsCreateDTO;
import com.ruh.mis.model.DTO.ResultsDTO;
import com.ruh.mis.model.Results;
import com.ruh.mis.repository.ResultsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultsServiceImpl implements ResultsService {

    @Autowired
    private ResultsRepository resultsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ResultsDTO> findAll() {
        return resultsRepository.findAll().stream()
                .map(results -> modelMapper.map(results, ResultsDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResultsDTO findById(int theId) {
        Results results = resultsRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Results not found: " + theId));
        return modelMapper.map(results, ResultsDTO.class);
    }

    @Override
    public Results save(ResultsCreateDTO theResultsCreateDTO) {
        Results results = modelMapper.map(theResultsCreateDTO, Results.class);
        return resultsRepository.save(results);
    }

    @Override
    public void deleteById(int theId) {
        resultsRepository.deleteById(theId);
    }
}
