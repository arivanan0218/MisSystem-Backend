package com.ruh.mis.service;

import com.ruh.mis.model.DTO.EndExamMarksCreateDTO;
import com.ruh.mis.model.DTO.EndExamMarksDTO;
import com.ruh.mis.model.DTO.MarksResponseDTO;
import com.ruh.mis.model.EndExamMarks;
import com.ruh.mis.model.Student;
import com.ruh.mis.model.EndExam;
import com.ruh.mis.repository.EndExamMarksRepository;
import com.ruh.mis.repository.StudentRepository;
import com.ruh.mis.repository.EndExamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EndExamMarksServiceImpl implements EndExamMarksService {

    @Autowired
    private EndExamMarksRepository endExamMarksRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EndExamRepository endExamRepository;

    @Autowired
    private MarksService marksService; // Inject MarksService to fetch assignment marks

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveEndExamMarksList(List<EndExamMarksCreateDTO> marksList) {
        for (EndExamMarksCreateDTO dto : marksList) {
            EndExamMarks marks = modelMapper.map(dto, EndExamMarks.class);

            Optional<Student> studentOpt = studentRepository.findById(dto.getStudentId());
            Optional<EndExam> endExamOpt = endExamRepository.findById(dto.getEndExamId());

            if (studentOpt.isEmpty() || endExamOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid Student ID or EndExam ID");
            }

            marks.setStudent(studentOpt.get());
            marks.setEndExam(endExamOpt.get());

            endExamMarksRepository.save(marks);
        }
    }

    @Override
    public List<EndExamMarksDTO> getEndExamMarksForStudent(int studentId) {
        return endExamMarksRepository.findByStudentId(studentId).stream()
                .map(marks -> {
                    EndExamMarksDTO dto = modelMapper.map(marks, EndExamMarksDTO.class);
                    dto.setStudentName(marks.getStudent().getStudent_name());
                    dto.setEndExamName(marks.getEndExam().getEndExamName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EndExamMarksDTO updateEndExamMarks(EndExamMarksDTO marksDTO) {
        EndExamMarks existing = endExamMarksRepository.findById(marksDTO.getId())
                .orElseThrow(() -> new RuntimeException("EndExam Marks not found: " + marksDTO.getId()));

        existing.setMarksObtained(marksDTO.getMarksObtained());
        EndExamMarks updated = endExamMarksRepository.save(existing);
        return modelMapper.map(updated, EndExamMarksDTO.class);
    }

    @Override
    public void deleteEndExamMarksById(int id) {
        endExamMarksRepository.deleteById(id);
    }

    // Method to calculate final module marks
    public double calculateFinalModuleMarks(int studentId, int moduleId) {
        // Fetch assignment marks for the student
        MarksResponseDTO assignmentMarks = marksService.getMarksForStudent(studentId);

        // Fetch end exam marks for the student and module
        List<EndExamMarks> endExamMarks = endExamMarksRepository.findByStudentId(studentId);

        // Calculate final assignment marks (from MarksResponseDTO)
        double finalAssignmentMarks = assignmentMarks.getFinalAssignmentMarks();

        // Calculate final end exam marks
        double finalEndExamMarks = endExamMarks.stream()
                .filter(mark -> mark.getEndExam().getModule().getId() == moduleId)
                .mapToDouble(mark -> mark.getMarksObtained() * (mark.getEndExam().getEndExamPercentage() / 100.0))
                .sum();

        // Return the sum of both
        return finalAssignmentMarks + finalEndExamMarks;
    }
}