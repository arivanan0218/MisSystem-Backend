package com.ruh.mis.service;

import com.ruh.mis.model.*;
import com.ruh.mis.model.Module;
import com.ruh.mis.repository.*;
import com.ruh.mis.model.DTO.ModuleResultDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuleResultServiceImpl implements ModuleResultService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private ModuleResultRepository moduleResultRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public void calculateModuleResults(int departmentId, int intakeId, int semesterId, int moduleId) {
        List<Assignment> assignments = assignmentRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                departmentId, intakeId, semesterId, moduleId);

        if (assignments.isEmpty()) return;

        Department department = assignments.get(0).getDepartment();
        Intake intake = assignments.get(0).getIntake();
        Semester semester = assignments.get(0).getSemester();
        Module module = assignments.get(0).getModule();

        List<Marks> allMarks = assignments.stream()
                .flatMap(a -> marksRepository.findByAssignment_Id(a.getId()).stream())
                .collect(Collectors.toList());

        Map<Student, List<Marks>> marksByStudent = allMarks.stream()
                .collect(Collectors.groupingBy(Marks::getStudent));

        marksByStudent.forEach((student, studentMarks) -> {
            double finalMarks = studentMarks.stream()
                    .mapToDouble(m -> m.getMarksObtained() * (m.getAssignment().getAssignmentPercentage() / 100.0))
                    .sum();

            String grade = calculateGrade(finalMarks);
            double gradePoint = calculateGradePoint(grade);

            Optional<ModuleResult> existing = moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndStudentId(
                    department.getId(), intake.getId(), semester.getId(), module.getId(), student.getId());

            ModuleResult result = existing.orElseGet(ModuleResult::new);
            result.setDepartment(department);
            result.setIntake(intake);
            result.setSemester(semester);
            result.setModule(module);
            result.setStudent(student);
            result.setFinalMarks(finalMarks);
            result.setGrade(grade);
            result.setGradePoint(gradePoint);

            moduleResultRepository.save(result);
        });
    }

    private String calculateGrade(double marks) {
        if (marks >= 85) return "A";
        else if (marks >= 70) return "B";
        else if (marks >= 55) return "C";
        else if (marks >= 40) return "D";
        else return "F";
    }

    private double calculateGradePoint(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }

    @Override
    public List<ModuleResultDTO> getModuleResults(int departmentId, int intakeId, int semesterId, int moduleId) {
        List<ModuleResult> results = moduleResultRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
                departmentId, intakeId, semesterId, moduleId);

        return results.stream().map(result -> {
            ModuleResultDTO dto = modelMapper.map(result, ModuleResultDTO.class);
            dto.setDepartmentName(result.getDepartment().getDepartmentName());
            dto.setIntakeName(result.getIntake().getIntakeYear());
            dto.setSemesterName(result.getSemester().getSemesterName());
            dto.setModuleName(result.getModule().getModuleName());
            dto.setStudentName(result.getStudent().getStudent_name());
            return dto;
        }).collect(Collectors.toList());
    }
}