package com.ruh.mis.service;

import com.ruh.mis.model.DTO.SemesterResultsDTO;
import java.util.List;

public interface SemesterResultsService {
    void calculateSemesterResults(int departmentId, int intakeId, int semesterId);
    SemesterResultsDTO getSemesterResultsByStudent(int departmentId, int intakeId, int semesterId, int studentId);
    List<SemesterResultsDTO> getSemesterResults(int departmentId, int intakeId, int semesterId);
}
