package com.ruh.mis.service;

import com.ruh.mis.model.DTO.*;
import com.ruh.mis.model.EndExamMarks;
import com.ruh.mis.model.Student;
import com.ruh.mis.model.EndExam;
import com.ruh.mis.repository.EndExamMarksRepository;
import com.ruh.mis.repository.ModuleRepository;
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
    private ModuleRepository moduleRepository; // Inject ModuleRepository

    @Autowired
    private MarksService marksService; // Inject MarksService to fetch assignment marks

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<EndExamMarksDTO> findAll() {
        return endExamMarksRepository.findAll().stream()
                .map(marks -> modelMapper.map(marks, EndExamMarksDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EndExamMarksDTO findById(int id) {
        EndExamMarks endexammarks = endExamMarksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EndExamMarks not found: " + id));
        return modelMapper.map(endexammarks, EndExamMarksDTO.class);
    }

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

    // Helper method to calculate grade based on GPA_Status and final marks
    private String calculateGrade(double finalMarks, String gpaStatus) {
        if ("GPA".equalsIgnoreCase(gpaStatus)) {
            if (finalMarks >= 85.00) {
                return "A+";
            } else if (finalMarks >= 70.00) {
                return "A";
            } else if (finalMarks >= 65.00) {
                return "A-";
            } else if (finalMarks >= 60.00) {
                return "B+";
            } else if (finalMarks >= 55.00) {
                return "B";
            } else if (finalMarks >= 50.00) {
                return "B-";
            } else if (finalMarks >= 45.00) {
                return "C+";
            } else if (finalMarks >= 40.00) {
                return "C";
            } else if (finalMarks >= 35.00) {
                return "C-";
            } else {
                return "E";
            }
        } else if ("NGPA".equalsIgnoreCase(gpaStatus)) {
            if (finalMarks >= 75.00) {
                return "H";
            } else if (finalMarks >= 50.00) {
                return "M";
            } else if (finalMarks >= 30.00) {
                return "S";
            } else {
                return "E";
            }
        } else {
            throw new IllegalArgumentException("Invalid GPA_Status: " + gpaStatus);
        }
    }

    // Helper method to calculate grade points (only for GPA modules)
    private Double calculateGradePoint(double finalMarks, String gpaStatus) {
        if ("GPA".equalsIgnoreCase(gpaStatus)) {
            if (finalMarks >= 85.00) {
                return 4.0;
            } else if (finalMarks >= 70.00) {
                return 4.0;
            } else if (finalMarks >= 65.00) {
                return 3.7;
            } else if (finalMarks >= 60.00) {
                return 3.3;
            } else if (finalMarks >= 55.00) {
                return 3.0;
            } else if (finalMarks >= 50.00) {
                return 2.7;
            } else if (finalMarks >= 45.00) {
                return 2.3;
            } else if (finalMarks >= 40.00) {
                return 2.0;
            } else if (finalMarks >= 35.00) {
                return 1.7;
            } else {
                return 0.0;
            }
        } else {
            return null; // NGPA modules do not contribute to grade points
        }
    }

    @Override
    public EndExamMarksResponseDTO getFinalEndExamMarksForStudent(int studentId) {
        List<EndExamMarks> endExamMarksList = endExamMarksRepository.findByStudentId(studentId);

        if (endExamMarksList.isEmpty()) {
            return new EndExamMarksResponseDTO(studentId, "Unknown", List.of(), 0.0);
        }

        String studentName = endExamMarksList.get(0).getStudent().getStudent_name();

        // Map each EndExamMarks entity to EndExamMarksDTO including moduleId
        List<EndExamMarksDTO> endExamMarksDTOs = endExamMarksList.stream()
                .map(marks -> {
                    EndExamMarksDTO dto = modelMapper.map(marks, EndExamMarksDTO.class);
                    dto.setStudent_name(marks.getStudent().getStudent_name());
                    dto.setEndExamName(marks.getEndExam().getEndExamName());
                    dto.setModuleId(marks.getEndExam().getModule().getId()); // Add moduleId to DTO
                    return dto;
                })
                .collect(Collectors.toList());

        // Calculate final end exam marks by summing weighted marks
        double finalEndExamMarks = endExamMarksList.stream()
                .mapToDouble(marks -> marks.getMarksObtained() * (marks.getEndExam().getEndExamPercentage() / 100.0))
                .sum();

        return new EndExamMarksResponseDTO(studentId, studentName, endExamMarksDTOs, finalEndExamMarks);
    }

//    @Override
//    public ModuleMarksResponseDTO calculateFinalModuleMarks(int studentId, int moduleId) {
//        // Fetch assignment marks for the student
//        MarksResponseDTO assignmentMarks = marksService.getMarksForStudent(studentId);
//
//        // Fetch end exam marks for the student and module
//        EndExamMarksResponseDTO endExamMarksResponse = getFinalEndExamMarksForStudent(studentId);
//
//        // Filter end exam marks for the specific module
//        List<EndExamMarksDTO> moduleEndExamMarks = endExamMarksResponse.getEndExamMarks().stream()
//                .filter(mark -> mark.getModuleId() == moduleId)
//                .collect(Collectors.toList());
//
//        // Calculate final end exam marks for the module
//        double finalEndExamMarks = moduleEndExamMarks.stream()
//                .mapToDouble(mark -> mark.getMarksObtained() * (mark.getEndExamPercentage() / 100.0))
//                .sum();
//
//        // Calculate final module marks
//        double finalModuleMarks = assignmentMarks.getFinalAssignmentMarks() + finalEndExamMarks;
//
//        // Fetch module details (moduleName, moduleCode, and GPA_Status)
//        com.ruh.mis.model.Module module = moduleRepository.findById(moduleId) // Use fully qualified name
//                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + moduleId));
//
//        String moduleName = module.getModuleName();
//        String moduleCode = module.getModuleCode();
//        String gpaStatus = module.getGPA_Status();
//        String grade = calculateGrade(finalModuleMarks, gpaStatus);
//        Double gradePoint = calculateGradePoint(finalModuleMarks, gpaStatus);
//        int credit = module.getCredit();
//
//        // Build and return the ModuleMarksResponseDTO
//        return new ModuleMarksResponseDTO(
//                studentId,
//                assignmentMarks.getStudent_name(),
//                moduleEndExamMarks,
//                finalEndExamMarks,
//                assignmentMarks.getAssignmentMarks(),
//                assignmentMarks.getFinalAssignmentMarks(),
//                finalModuleMarks,
//                module.getModuleName(),
//                module.getModuleCode(),
//                module.getCredit(),
//                module.getGPA_Status(),
//                grade,
//                gradePoint
//        );
//    }


    @Override
    public ModuleMarksResponseDTO calculateFinalModuleMarks(int studentId, int moduleId) {
        // Fetch assignment marks for the student
        MarksResponseDTO assignmentMarksResponse = marksService.getMarksForStudent(studentId);

        // Filter assignment marks for the current module
        List<AssignmentMarksDTO> moduleAssignmentMarks = assignmentMarksResponse.getAssignmentMarks().stream()
                .filter(am -> am.getModuleId() == moduleId)
                .collect(Collectors.toList());

        // Calculate weighted assignment marks for the module
        double finalAssignmentMarks = moduleAssignmentMarks.stream()
                .mapToDouble(am -> am.getMarksObtained() * (am.getAssignmentPercentage() / 100.0))
                .sum();

        // Fetch and filter end exam marks for the module
        EndExamMarksResponseDTO endExamMarksResponse = getFinalEndExamMarksForStudent(studentId);
        List<EndExamMarksDTO> moduleEndExamMarks = endExamMarksResponse.getEndExamMarks().stream()
                .filter(mark -> mark.getModuleId() == moduleId)
                .collect(Collectors.toList());

        // Calculate weighted end exam marks
        double finalEndExamMarks = moduleEndExamMarks.stream()
                .mapToDouble(mark -> mark.getMarksObtained() * (mark.getEndExamPercentage() / 100.0))
                .sum();

        // Calculate total module marks
        double finalModuleMarks = finalAssignmentMarks + finalEndExamMarks;

        // Fetch module details
        com.ruh.mis.model.Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found: " + moduleId));

        // Calculate grade and grade points
        String gpaStatus = module.getGPA_Status();
        String grade = calculateGrade(finalModuleMarks, gpaStatus);
        Double gradePoint = calculateGradePoint(finalModuleMarks, gpaStatus);

        return new ModuleMarksResponseDTO(
                studentId,
                assignmentMarksResponse.getStudent_name(),
                moduleEndExamMarks,
                finalEndExamMarks,
                moduleAssignmentMarks,
                finalAssignmentMarks,
                finalModuleMarks,
                module.getModuleName(),
                module.getModuleCode(),
                module.getCredit(),
                gpaStatus,
                grade,
                gradePoint
        );
    }

    @Override
    public List<EndExamMarksDTO> getEndExamMarksForStudent(int studentId) {
        return endExamMarksRepository.findByStudentId(studentId).stream()
                .map(marks -> {
                    EndExamMarksDTO dto = modelMapper.map(marks, EndExamMarksDTO.class);
                    dto.setStudent_name(marks.getStudent().getStudent_name());
                    dto.setEndExamName(marks.getEndExam().getEndExamName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}