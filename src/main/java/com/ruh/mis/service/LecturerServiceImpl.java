package com.ruh.mis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.LecturerDTO;
import com.ruh.mis.model.Department;
import com.ruh.mis.model.Lecturer;
import com.ruh.mis.repository.DepartmentRepository;
import com.ruh.mis.repository.LecturerRepository;

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<LecturerDTO> findAll() {
        return lecturerRepository.findAll().stream()
                .map(lecturer -> modelMapper.map(lecturer, LecturerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public LecturerDTO findById(int theId) {
        Lecturer lecturer = lecturerRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found: " + theId));
        return modelMapper.map(lecturer, LecturerDTO.class);
    }

    @Override
    public Lecturer save(LecturerCreateDTO theLecturerCreateDTO) {
        // Map DTO to entity
        Lecturer lecturer = modelMapper.map(theLecturerCreateDTO, Lecturer.class);
        
        // Set department manually since ModelMapper can't handle this relationship properly
        Department department = departmentRepository.findById(theLecturerCreateDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found: " + theLecturerCreateDTO.getDepartmentId()));
        lecturer.setDepartment(department);
        
        // Save lecturer info
        Lecturer savedLecturer = lecturerRepository.save(lecturer);

        // Register lecturer as user
        userService.registerLecturerUser(theLecturerCreateDTO.getUsername(),
                theLecturerCreateDTO.getLecturerEmail(),
                theLecturerCreateDTO.getPassword());

        return savedLecturer;
    }

    @Override
    public void saveLecturersList(List<LecturerCreateDTO> lecturerCreateDTOList) {
        for (LecturerCreateDTO dto : lecturerCreateDTOList) {
            // Map DTO to entity
            Lecturer lecturer = modelMapper.map(dto, Lecturer.class);
            
            // Set department manually
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found: " + dto.getDepartmentId()));
            lecturer.setDepartment(department);
            
            // Save lecturer info
            lecturerRepository.save(lecturer);

            // Register lecturer as user
            userService.registerLecturerUser(dto.getUsername(), dto.getLecturerEmail(), dto.getPassword());
        }
    }

    @Override
    public void deleteById(int theId) {
        lecturerRepository.deleteById(theId);
    }

    @Override
    public List<LecturerDTO> findByDepartmentId(int departmentId) {
        List<Lecturer> lecturers = lecturerRepository.findByDepartment_Id(departmentId);
        return lecturers.stream()
                .map(lecturer -> modelMapper.map(lecturer, LecturerDTO.class))
                .collect(Collectors.toList());
    }
}