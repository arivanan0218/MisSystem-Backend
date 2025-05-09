package com.ruh.mis.service;

import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.SemesterDTO;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.Intake;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.IntakeRepository;
import com.ruh.mis.repository.SemesterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SemesterDTO> findAll() {
        return semesterRepository.findAll().stream()
                .map(semester -> {
                    SemesterDTO dto = modelMapper.map(semester, SemesterDTO.class);

                    // Explicitly set entity IDs
                    if (semester.getDepartment() != null) {
                        dto.setDepartmentId(semester.getDepartment().getId());
                    }

                    if (semester.getIntake() != null) {
                        dto.setIntakeId(semester.getIntake().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SemesterDTO findById(int theId) {
        Semester semester = semesterRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Semester not found: " + theId));

        SemesterDTO dto = modelMapper.map(semester, SemesterDTO.class);

        // Explicitly set entity IDs
        if (semester.getDepartment() != null) {
            dto.setDepartmentId(semester.getDepartment().getId());
        }

        if (semester.getIntake() != null) {
            dto.setIntakeId(semester.getIntake().getId());
        }

        return dto;
    }

    @Override
    public List<SemesterDTO> getSemestersByDepartmentIdAndIntakeId(int departmentId, int intakeId) {
        // Fetch semesters from the repository
        List<Semester> semesters = semesterRepository.findByDepartmentIdAndIntakeId(departmentId, intakeId);

        // Map entities to DTOs with explicit ID setting
        return semesters.stream()
                .map(semester -> {
                    SemesterDTO dto = modelMapper.map(semester, SemesterDTO.class);

                    // Explicitly set entity IDs
                    if (semester.getDepartment() != null) {
                        dto.setDepartmentId(semester.getDepartment().getId());
                    }

                    if (semester.getIntake() != null) {
                        dto.setIntakeId(semester.getIntake().getId());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Semester save(SemesterCreateDTO theSemesterCreateDTO) {
        // Create a new Semester entity
        Semester semester = modelMapper.map(theSemesterCreateDTO, Semester.class);

        // Fetch the required entities from repositories using their IDs from the DTO
        Department department = departmentRepository.findById(theSemesterCreateDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found: " + theSemesterCreateDTO.getDepartmentId()));

        Intake intake = intakeRepository.findById(theSemesterCreateDTO.getIntakeId())
                .orElseThrow(() -> new RuntimeException("Intake not found: " + theSemesterCreateDTO.getIntakeId()));

        // Set the relationships manually
        semester.setDepartment(department);
        semester.setIntake(intake);

        // Save the semester
        return semesterRepository.save(semester);
    }

    @Override
    public void deleteById(int theId) {
        semesterRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public SemesterDTO update(int semesterId, SemesterCreateDTO semesterCreateDTO) {
        // Find the existing department
        Semester existingSemester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found: " + semesterId));

        // Update the fields
        existingSemester.setSemesterName(semesterCreateDTO.getSemesterName());
        existingSemester.setSemesterYear(semesterCreateDTO.getSemesterYear());
        existingSemester.setSemesterDuration(semesterCreateDTO.getSemesterDuration());

        // Save the updated entity
        Semester updatedSemester = semesterRepository.save(existingSemester);

        // Map the updated entity to DTO and return with explicit ID setting
        SemesterDTO dto = modelMapper.map(updatedSemester, SemesterDTO.class);

        // Explicitly set entity IDs
        if (updatedSemester.getDepartment() != null) {
            dto.setDepartmentId(updatedSemester.getDepartment().getId());
        }

        if (updatedSemester.getIntake() != null) {
            dto.setIntakeId(updatedSemester.getIntake().getId());
        }

        return dto;
    }
}