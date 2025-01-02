package com.ruh.mis.controller;

import com.ruh.mis.model.DTO.ResultsCreateDTO;
import com.ruh.mis.model.DTO.ResultsDTO;
import com.ruh.mis.model.Results;
import com.ruh.mis.service.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultsController {

    @Autowired
    private ResultsService resultsService;

    @GetMapping("/")
    public List<ResultsDTO> findAll() {
        return resultsService.findAll();
    }

    @GetMapping("/{resultsId}")
    public ResultsDTO getResults(@PathVariable int resultsId) {
        ResultsDTO theResults = resultsService.findById(resultsId);

        if (theResults == null) {
            throw new RuntimeException("Results id not found: " + resultsId);
        }

        return theResults;
    }

    @PostMapping("/create")
    public ResultsDTO addResults(@RequestBody ResultsCreateDTO theResultsCreateDTO) {
        Results savedResults = resultsService.save(theResultsCreateDTO);
        return resultsService.findById(savedResults.getId());
    }

    @DeleteMapping("/{resultsId}")
    public String deleteResults(@PathVariable int resultsId) {
        ResultsDTO tempResults = resultsService.findById(resultsId);

        if (tempResults == null) {
            throw new RuntimeException("Results id not found: " + resultsId);
        }

        resultsService.deleteById(resultsId);

        return "Deleted results id: " + resultsId;
    }
}
