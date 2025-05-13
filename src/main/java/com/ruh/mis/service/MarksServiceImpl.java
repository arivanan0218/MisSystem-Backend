package com.ruh.mis.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.DTO.AssignmentMarksDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.DTO.MarksResponseDTO;
import com.ruh.mis.model.Marks;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.AssignmentRepository;
import com.ruh.mis.repository.MarksRepository;
import com.ruh.mis.repository.StudentRepository;

@Service
public class MarksServiceImpl implements MarksService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<MarksDTO> findAll() {
        return marksRepository.findAll().stream()
                .map(marks -> {
                    MarksDTO marksDTO = modelMapper.map(marks, MarksDTO.class);
                    // Fetch student name from the Student entity
                    marksDTO.setStudentId(marks.getStudent().getId());
                    marksDTO.setStudent_name(marks.getStudent().getStudentName());
                    // Also fetch assignment name
                    marksDTO.setAssignmentName(marks.getAssignment().getAssignmentName());
                    
                    return marksDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MarksDTO findById(int id) {
        Marks marks = marksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marks not found: " + id));
        MarksDTO marksDTO = modelMapper.map(marks, MarksDTO.class);
        // Fetch student name from the Student entity
        marksDTO.setStudentId(marks.getStudent().getId());
        marksDTO.setStudent_name(marks.getStudent().getStudentName());
        // Also fetch assignment name
        marksDTO.setAssignmentName(marks.getAssignment().getAssignmentName());
        
        return marksDTO;
    }

    @Override
    public void saveMarksList(List<MarksCreateDTO> marksCreateDTOList) {
        for (MarksCreateDTO marksCreateDTO : marksCreateDTOList) {
            // Check if marks already exist for this student and assignment
            List<Marks> existingMarks = marksRepository.findByStudentIdAndAssignmentId(
                    marksCreateDTO.getStudentId(), marksCreateDTO.getAssignmentId());
            
            if (!existingMarks.isEmpty()) {
                // Update existing marks instead of creating new ones
                Marks marks = existingMarks.get(0);
                marks.setMarksObtained(marksCreateDTO.getMarksObtained());
                marksRepository.save(marks);
            } else {
                // Create new marks
                Marks marks = new Marks();
                
                Optional<Student> studentOpt = studentRepository.findById(marksCreateDTO.getStudentId());
                Optional<Assignment> assignmentOpt = assignmentRepository.findById(marksCreateDTO.getAssignmentId());

                if (studentOpt.isEmpty() || assignmentOpt.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Invalid Student ID: " + marksCreateDTO.getStudentId() + 
                            " or Assignment ID: " + marksCreateDTO.getAssignmentId());
                }

                marks.setStudent(studentOpt.get());
                marks.setAssignment(assignmentOpt.get());
                marks.setMarksObtained(marksCreateDTO.getMarksObtained());

                marksRepository.save(marks);
            }
        }
    }

    @Override
    public MarksResponseDTO getMarksForStudent(int studentId) {
        List<Marks> marksList = marksRepository.findByStudentId(studentId);

        if (marksList.isEmpty()) {
            // Get student details if available, even if no marks exist
            Optional<Student> student = studentRepository.findById(studentId);
            if (student.isPresent()) {
                return new MarksResponseDTO(studentId, student.get().getStudentName(), List.of(), 0);
            }
            return new MarksResponseDTO(studentId, "Unknown", List.of(), 0);
        }

        String studentName = marksList.get(0).getStudent().getStudentName();
        List<AssignmentMarksDTO> assignmentMarks = marksList.stream()
                .map(marks -> new AssignmentMarksDTO(
                        marks.getAssignment().getId(),
                        marks.getAssignment().getAssignmentName(),
                        marks.getMarksObtained(),
                        marks.getAssignment().getAssignmentPercentage(),
                        marks.getAssignment().getModule().getId()
                ))
                .collect(Collectors.toList());

        double finalAssignmentMarks = marksList.stream()
                .mapToDouble(marks -> marks.getMarksObtained() * (marks.getAssignment().getAssignmentPercentage() / 100.0))
                .sum();

        return new MarksResponseDTO(studentId, studentName, assignmentMarks, finalAssignmentMarks);
    }

    @Override
    public MarksDTO updateMarks(int id, MarksCreateDTO marksCreateDTO) {
        Marks existingMarks = marksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marks not found: " + id));

        // Update the marks
        existingMarks.setMarksObtained(marksCreateDTO.getMarksObtained());

        // Fetch and set student and assignment if they've changed
        if (existingMarks.getStudent().getId() != marksCreateDTO.getStudentId() ||
            existingMarks.getAssignment().getId() != marksCreateDTO.getAssignmentId()) {
            
            Optional<Student> studentOpt = studentRepository.findById(marksCreateDTO.getStudentId());
            Optional<Assignment> assignmentOpt = assignmentRepository.findById(marksCreateDTO.getAssignmentId());

            if (studentOpt.isEmpty() || assignmentOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid Student ID or Assignment ID");
            }

            existingMarks.setStudent(studentOpt.get());
            existingMarks.setAssignment(assignmentOpt.get());
        }

        // Save the updated marks
        Marks updatedMarks = marksRepository.save(existingMarks);
        MarksDTO marksDTO = modelMapper.map(updatedMarks, MarksDTO.class);
        // Fetch student name from the Student entity
        marksDTO.setStudentId(updatedMarks.getStudent().getId());
        marksDTO.setStudent_name(updatedMarks.getStudent().getStudentName());
        // Also fetch assignment name
        marksDTO.setAssignmentName(updatedMarks.getAssignment().getAssignmentName());

        return marksDTO;
    }

    @Override
    public void deleteById(int id) {
        marksRepository.deleteById(id);
    }
}