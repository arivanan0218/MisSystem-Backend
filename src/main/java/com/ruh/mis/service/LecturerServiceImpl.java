package com.ruh.mis.service;

import com.ruh.mis.model.DTO.LecturerCreateDTO;
import com.ruh.mis.model.DTO.LecturerDTO;
import com.ruh.mis.model.Lecturer;
import com.ruh.mis.repository.LecturerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

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
        // Save lecturer info
        Lecturer lecturer = modelMapper.map(theLecturerCreateDTO, Lecturer.class);
        lecturerRepository.save(lecturer);

        // Register lecturer as user
        userService.registerLecturerUser(theLecturerCreateDTO.getUsername(),
                theLecturerCreateDTO.getEmail(),
                theLecturerCreateDTO.getPassword());

        return lecturer;
    }

    @Override
    public void saveLecturersList(List<LecturerCreateDTO> lecturerCreateDTOList) {
        for (LecturerCreateDTO dto : lecturerCreateDTOList) {
            // Save lecturer info
            Lecturer lecturer = modelMapper.map(dto, Lecturer.class);
            lecturerRepository.save(lecturer);

            // Register lecturer as user
            userService.registerLecturerUser(dto.getUsername(), dto.getEmail(), dto.getPassword());
        }
    }

    @Override
    public void deleteById(int theId) {
        lecturerRepository.deleteById(theId);
    }

    @Override
    public List<LecturerDTO> findByDepartmentId(int departmentId) {
        List<Lecturer> lecturers = lecturerRepository.findByDepartmentId(departmentId);
        return lecturers.stream()
                .map(lecturer -> modelMapper.map(lecturer, LecturerDTO.class))
                .collect(Collectors.toList());
    }
}
