package com.ruh.mis.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    MarksDTO marksDTO = new MarksDTO();
                    marksDTO.setId(marks.getId());
                    marksDTO.setStudentId(marks.getStudent().getId());
                    marksDTO.setAssignmentId(marks.getAssignment().getId());
                    marksDTO.setStudent_name(marks.getStudent().getStudentName());
                    marksDTO.setStudent_Reg_No(marks.getStudent().getStudentRegNo());
                    marksDTO.setAssignmentName(marks.getAssignment().getAssignmentName());
                    marksDTO.setMarksObtained(marks.getMarksObtained());
                    return marksDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MarksDTO> findByAssignmentId(int assignmentId) {
        // Verify assignment exists
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found: " + assignmentId));
                
        // Get marks for the assignment
        List<Marks> marksList = marksRepository.findByAssignment_Id(assignmentId);
        
        // Map to DTOs with complete information
        return marksList.stream()
                .map(marks -> {
                    MarksDTO marksDTO = new MarksDTO();
                    marksDTO.setId(marks.getId());
                    marksDTO.setStudentId(marks.getStudent().getId());
                    marksDTO.setAssignmentId(assignmentId);
                    marksDTO.setStudent_name(marks.getStudent().getStudentName());
                    marksDTO.setStudent_Reg_No(marks.getStudent().getStudentRegNo());
                    marksDTO.setAssignmentName(assignment.getAssignmentName());
                    marksDTO.setMarksObtained(marks.getMarksObtained());
                    return marksDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MarksDTO findById(int id) {
        Marks marks = marksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marks not found: " + id));
                
        MarksDTO marksDTO = new MarksDTO();
        marksDTO.setId(marks.getId());
        marksDTO.setStudentId(marks.getStudent().getId());
        marksDTO.setAssignmentId(marks.getAssignment().getId());
        marksDTO.setStudent_name(marks.getStudent().getStudentName());
        marksDTO.setStudent_Reg_No(marks.getStudent().getStudentRegNo());
        marksDTO.setAssignmentName(marks.getAssignment().getAssignmentName());
        marksDTO.setMarksObtained(marks.getMarksObtained());
        
        return marksDTO;
    }

    @Override
    @Transactional
    public void saveMarksList(List<MarksCreateDTO> marksCreateDTOList) {
        if (marksCreateDTOList == null || marksCreateDTOList.isEmpty()) {
            throw new IllegalArgumentException("Marks list cannot be empty");
        }
        
        for (MarksCreateDTO marksCreateDTO : marksCreateDTOList) {
            // Validate input data
            if (marksCreateDTO.getStudentId() <= 0) {
                throw new IllegalArgumentException("Invalid student ID: " + marksCreateDTO.getStudentId());
            }
            
            if (marksCreateDTO.getAssignmentId() <= 0) {
                throw new IllegalArgumentException("Invalid assignment ID: " + marksCreateDTO.getAssignmentId());
            }
            
            if (marksCreateDTO.getMarksObtained() < 0) {
                throw new IllegalArgumentException("Marks obtained cannot be negative");
            }
            
            // Check if student and assignment exist
            Optional<Student> studentOpt = studentRepository.findById(marksCreateDTO.getStudentId());
            Optional<Assignment> assignmentOpt = assignmentRepository.findById(marksCreateDTO.getAssignmentId());

            if (studentOpt.isEmpty() || assignmentOpt.isEmpty()) {
                throw new IllegalArgumentException(
                        "Invalid Student ID: " + marksCreateDTO.getStudentId() + 
                        " or Assignment ID: " + marksCreateDTO.getAssignmentId());
            }

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
                marks.setStudent(studentOpt.get());
                marks.setAssignment(assignmentOpt.get());
                marks.setMarksObtained(marksCreateDTO.getMarksObtained());
                marksRepository.save(marks);
            }
        }
    }

    @Override
    public MarksResponseDTO getMarksForStudent(int studentId) {
        // Validate student exists
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found: " + studentId);
        }

        List<Marks> marksList = marksRepository.findByStudentId(studentId);

        if (marksList.isEmpty()) {
            // Get student details if available, even if no marks exist
            Student student = studentOpt.get();
            return new MarksResponseDTO(studentId, student.getStudentName(), List.of(), 0);
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
    @Transactional
    public MarksDTO updateMarks(int id, MarksCreateDTO marksCreateDTO) {
        // Validate mark exists
        Marks existingMarks = marksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marks not found: " + id));

        // Validate input data
        if (marksCreateDTO.getStudentId() <= 0) {
            throw new IllegalArgumentException("Invalid student ID: " + marksCreateDTO.getStudentId());
        }
        
        if (marksCreateDTO.getAssignmentId() <= 0) {
            throw new IllegalArgumentException("Invalid assignment ID: " + marksCreateDTO.getAssignmentId());
        }
        
        if (marksCreateDTO.getMarksObtained() < 0) {
            throw new IllegalArgumentException("Marks obtained cannot be negative");
        }

        // Update the marks
        existingMarks.setMarksObtained(marksCreateDTO.getMarksObtained());

        // Fetch and set student and assignment if they've changed
        if (existingMarks.getStudent().getId() != marksCreateDTO.getStudentId() ||
            existingMarks.getAssignment().getId() != marksCreateDTO.getAssignmentId()) {
            
            Optional<Student> studentOpt = studentRepository.findById(marksCreateDTO.getStudentId());
            Optional<Assignment> assignmentOpt = assignmentRepository.findById(marksCreateDTO.getAssignmentId());

            if (studentOpt.isEmpty() || assignmentOpt.isEmpty()) {
                throw new IllegalArgumentException(
                        "Invalid Student ID: " + marksCreateDTO.getStudentId() + 
                        " or Assignment ID: " + marksCreateDTO.getAssignmentId());
            }

            existingMarks.setStudent(studentOpt.get());
            existingMarks.setAssignment(assignmentOpt.get());
        }

        // Save the updated marks
        Marks updatedMarks = marksRepository.save(existingMarks);
        
        // Create and return DTO with complete information
        MarksDTO marksDTO = new MarksDTO();
        marksDTO.setId(updatedMarks.getId());
        marksDTO.setStudentId(updatedMarks.getStudent().getId());
        marksDTO.setAssignmentId(updatedMarks.getAssignment().getId());
        marksDTO.setStudent_name(updatedMarks.getStudent().getStudentName());
        marksDTO.setStudent_Reg_No(updatedMarks.getStudent().getStudentRegNo());
        marksDTO.setAssignmentName(updatedMarks.getAssignment().getAssignmentName());
        marksDTO.setMarksObtained(updatedMarks.getMarksObtained());

        return marksDTO;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        // Verify mark exists before deletion
        if (!marksRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete - Marks not found: " + id);
        }
        
        marksRepository.deleteById(id);
    }
}