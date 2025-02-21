package com.ruh.mis.service;

import com.ruh.mis.model.DTO.EndExamCreateDTO;
import com.ruh.mis.model.DTO.EndExamDTO;
import com.ruh.mis.model.EndExam;
import com.ruh.mis.repository.EndExamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndExamServiceImpl implements EndExamService {

    @Autowired private EndExamRepository endExamRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public List<EndExamDTO> findAll() {
        return endExamRepository.findAll().stream()
                .map(exam -> modelMapper.map(exam, EndExamDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EndExamDTO findById(int id) {
        EndExam exam = endExamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EndExam not found: " + id));
        return modelMapper.map(exam, EndExamDTO.class);
    }

    @Override
    public List<EndExamDTO> getEndExamByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
            int departmentId, int intakeId, int semesterId, int moduleId) {
        return endExamRepository
                .findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                        departmentId, intakeId, semesterId, moduleId)
                .stream()
                .map(exam -> modelMapper.map(exam, EndExamDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EndExamDTO save(EndExamCreateDTO dto) {
        List<EndExam> existing = endExamRepository
                .findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                        dto.getDepartmentId(),
                        dto.getIntakeId(),
                        dto.getSemesterId(),
                        dto.getModuleId()
                );

        if (!existing.isEmpty()) {
            throw new RuntimeException("EndExam already exists for this module");
        }

        EndExam exam = modelMapper.map(dto, EndExam.class);
        EndExam savedExam = endExamRepository.save(exam);
        return modelMapper.map(savedExam, EndExamDTO.class); // Convert to DTO before returning
    }

    @Override
    @Transactional
    public EndExamDTO update(int id, EndExamCreateDTO dto) {
        EndExam existing = endExamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EndExam not found: " + id));

        existing.setEndExamName(dto.getEndExamName());
        existing.setEndExamPercentage(dto.getEndExamPercentage());

        EndExam updated = endExamRepository.save(existing);
        return modelMapper.map(updated, EndExamDTO.class);
    }

    @Override
    public void deleteById(int id) {
        endExamRepository.deleteById(id);
    }
}