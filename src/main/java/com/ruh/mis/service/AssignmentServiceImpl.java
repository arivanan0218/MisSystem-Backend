package com.ruh.mis.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Module;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.DTO.AssignmentCreateDTO;
import com.ruh.mis.model.DTO.AssignmentDTO;
import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.AssignmentRepository;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.IntakeRepository;
import com.ruh.mis.repository.ModuleRepository;
import com.ruh.mis.repository.SemesterRepository;
import com.ruh.mis.repository.StudentRepository;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AssignmentDTO> findAll() {
        return assignmentRepository.findAll().stream()
                .map(assignment -> {
                    AssignmentDTO dto = modelMapper.map(assignment, AssignmentDTO.class);

                    // Explicitly set entity IDs
                    if (assignment.getDepartment() != null) {
                        dto.setDepartmentId(assignment.getDepartment().getId());
                    }

                    if (assignment.getIntake() != null) {
                        dto.setIntakeId(assignment.getIntake().getId());
                    }

                    if (assignment.getSemester() != null) {
                        dto.setSemesterId(assignment.getSemester().getId());
                    }

                    if (assignment.getModule() != null) {
                        dto.setModuleId(assignment.getModule().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MarksDTO> findAllMarks() {
        return assignmentRepository.findAll().stream()
                .map(assignment -> {
                    MarksDTO marksDTO = modelMapper.map(assignment, MarksDTO.class);

                    if (!assignment.getStudents().isEmpty()) {
                        Student student = assignment.getStudents().get(0);
                        marksDTO.setStudentId(Integer.parseInt(student.getStudentRegNo())); // Using new field name
                        marksDTO.setStudent_name(student.getStudentName()); // Using new field name
                    }
                    return marksDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentDTO findById(int theId) {
        Assignment assignment = assignmentRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Assignment not found: " + theId));

        AssignmentDTO dto = modelMapper.map(assignment, AssignmentDTO.class);

        // Explicitly set entity IDs
        if (assignment.getDepartment() != null) {
            dto.setDepartmentId(assignment.getDepartment().getId());
        }

        if (assignment.getIntake() != null) {
            dto.setIntakeId(assignment.getIntake().getId());
        }

        if (assignment.getSemester() != null) {
            dto.setSemesterId(assignment.getSemester().getId());
        }

        if (assignment.getModule() != null) {
            dto.setModuleId(assignment.getModule().getId());
        }

        return dto;
    }

    @Override
    public List<AssignmentDTO> getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(int departmentId, int intakeId, int semesterId, int moduleId) {
        List<Assignment> assignments = assignmentRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(departmentId, intakeId, semesterId, moduleId);

        return assignments.stream()
                .map(assignment -> {
                    AssignmentDTO dto = modelMapper.map(assignment, AssignmentDTO.class);

                    // Explicitly set entity IDs
                    if (assignment.getDepartment() != null) {
                        dto.setDepartmentId(assignment.getDepartment().getId());
                    }

                    if (assignment.getIntake() != null) {
                        dto.setIntakeId(assignment.getIntake().getId());
                    }

                    if (assignment.getSemester() != null) {
                        dto.setSemesterId(assignment.getSemester().getId());
                    }

                    if (assignment.getModule() != null) {
                        dto.setModuleId(assignment.getModule().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Assignment save(AssignmentCreateDTO theAssignmentCreateDTO) {
        // Create a new Assignment entity
        Assignment assignment = modelMapper.map(theAssignmentCreateDTO, Assignment.class);

        // Fetch the required entities from repositories using their IDs from the DTO
        Department department = departmentRepository.findById(theAssignmentCreateDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found: " + theAssignmentCreateDTO.getDepartmentId()));

        Intake intake = intakeRepository.findById(theAssignmentCreateDTO.getIntakeId())
                .orElseThrow(() -> new RuntimeException("Intake not found: " + theAssignmentCreateDTO.getIntakeId()));

        Semester semester = semesterRepository.findById(theAssignmentCreateDTO.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found: " + theAssignmentCreateDTO.getSemesterId()));

        Module module = moduleRepository.findById(theAssignmentCreateDTO.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module not found: " + theAssignmentCreateDTO.getModuleId()));

        // Set the relationships manually
        assignment.setDepartment(department);
        assignment.setIntake(intake);
        assignment.setSemester(semester);
        assignment.setModule(module);

        // Save the assignment
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

        // Map the updated entity to DTO and return with explicit ID setting
        AssignmentDTO dto = modelMapper.map(updatedAssignment, AssignmentDTO.class);

        // Explicitly set entity IDs
        if (updatedAssignment.getDepartment() != null) {
            dto.setDepartmentId(updatedAssignment.getDepartment().getId());
        }

        if (updatedAssignment.getIntake() != null) {
            dto.setIntakeId(updatedAssignment.getIntake().getId());
        }

        if (updatedAssignment.getSemester() != null) {
            dto.setSemesterId(updatedAssignment.getSemester().getId());
        }

        if (updatedAssignment.getModule() != null) {
            dto.setModuleId(updatedAssignment.getModule().getId());
        }

        return dto;
    }
}