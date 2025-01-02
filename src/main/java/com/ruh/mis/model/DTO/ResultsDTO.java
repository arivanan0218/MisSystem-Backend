package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultsDTO {
    private int id;
    private int moduleId;
    private double score;
    private String grade;
    private String GPA;
}
