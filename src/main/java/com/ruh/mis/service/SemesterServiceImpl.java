package com.ruh.mis.service;

import com.ruh.mis.model.DTO.SemesterCreateDTO;
import com.ruh.mis.model.DTO.SemesterDTO;
import com.ruh.mis.model.Semester;
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
    private ModelMapper modelMapper;

    @Override
    public List<SemesterDTO> findAll() {
        return semesterRepository.findAll().stream()
                .map(semester -> modelMapper.map(semester, SemesterDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SemesterDTO findById(int theId) {
        Semester semester = semesterRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Semester not found: " + theId));
        return modelMapper.map(semester, SemesterDTO.class);
    }

    @Override
    public List<SemesterDTO> getSemestersByDepartmentIdAndIntakeId(int departmentId, int intakeId) {
        // Fetch semesters from the repository
        List<Semester> semesters = semesterRepository.findByDepartmentIdAndIntakeId(departmentId, intakeId);

        // Map entities to DTOs
        return semesters.stream()
                .map(semester -> modelMapper.map(semester, SemesterDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Semester save(SemesterCreateDTO theSemesterCreateDTO) {
        Semester semester = modelMapper.map(theSemesterCreateDTO, Semester.class);
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

        // Map the updated entity to DTO and return
        return modelMapper.map(updatedSemester, SemesterDTO.class);
    }
}
