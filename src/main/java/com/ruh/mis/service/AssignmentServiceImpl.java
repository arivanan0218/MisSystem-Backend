package com.ruh.mis.service;

import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.AssignmentRepository;
import com.ruh.mis.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AssignmentDTO> findAll() {
        return assignmentRepository.findAll().stream()
                .map(assignment -> modelMapper.map(assignment, AssignmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MarksDTO> findAllMarks() {
        return assignmentRepository.findAll().stream()
                .map(assignment -> {
                    MarksDTO marksDTO = modelMapper.map(assignment, MarksDTO.class);

                    if (!assignment.getStudents().isEmpty()) {
                        Student student = assignment.getStudents().get(0);
                        marksDTO.setStudentId(Integer.parseInt(student.getStudent_Reg_No())); // ✅ Fixed field name
                        marksDTO.setStudent_name(student.getStudent_name());
                    }
                    return marksDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentDTO findById(int theId) {
        Assignment assignment = assignmentRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Assignment not found: " + theId));
        return modelMapper.map(assignment, AssignmentDTO.class);
    }

    @Override
    public List<AssignmentDTO> getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(int departmentId, int intakeId, int semesterId, int moduleId) {
        List<Assignment> assignments = assignmentRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(departmentId, intakeId, semesterId, moduleId);

        return assignments.stream()
                .map(assignment -> modelMapper.map(assignment, AssignmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Assignment save(AssignmentCreateDTO theAssignmentCreateDTO) {
        Assignment assignment = modelMapper.map(theAssignmentCreateDTO, Assignment.class);
        return assignmentRepository.save(assignment);
    }

    @Override
    public void saveMarksList(List<MarksCreateDTO> marksCreateDTOList) { // ✅ Fixed method name
        for (MarksCreateDTO marksCreateDTO : marksCreateDTOList) {
            Assignment assignment = modelMapper.map(marksCreateDTO, Assignment.class);

            Optional<Student> studentOpt = studentRepository.findById(marksCreateDTO.getStudentId());
            if (studentOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid student ID: " + marksCreateDTO.getStudentId());
            }
            assignment.setStudents(List.of(studentOpt.get()));

            assignmentRepository.save(assignment);
        }
    }

    @Override
    public void deleteById(int theId) {
        assignmentRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public AssignmentDTO update(int assignmentId, AssignmentCreateDTO assignmentCreateDTO) {
        // Find the existing assignment
        Assignment existingAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found: " + assignmentId));

        // Update the fields
        existingAssignment.setAssignmentName(assignmentCreateDTO.getAssignmentName());
        existingAssignment.setAssignmentPercentage(assignmentCreateDTO.getAssignmentPercentage());

        // Save the updated entity
        Assignment updatedAssignment = assignmentRepository.save(existingAssignment);

        // Map the updated entity to DTO and return
        return modelMapper.map(updatedAssignment, AssignmentDTO.class);
    }
}
