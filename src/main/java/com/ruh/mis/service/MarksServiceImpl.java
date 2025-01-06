package com.ruh.mis.service;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.Marks;
import com.ruh.mis.repository.MarksRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarksServiceImpl implements MarksService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<MarksDTO> findAll() {
        return marksRepository.findAll().stream()
                .map(marks -> modelMapper.map(marks, MarksDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MarksDTO findById(int theId) {
        Marks marks = marksRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Marks not found: " + theId));
        return modelMapper.map(marks, MarksDTO.class);
    }

    @Override
    public List<MarksDTO> getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleAndAssignmentId(int departmentId, int intakeId, int semesterId, int moduleId, int assignmentId) {
        List<Marks> markss = marksRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndAssignmentId(departmentId, intakeId, semesterId, moduleId, assignmentId);

        return markss.stream()
                .map(marks -> modelMapper.map(marks,MarksDTO.class))
                .collect(Collectors.toList());

    }

    @Override
    public Marks save(MarksCreateDTO theMarksCreateDTO) {
        Marks marks = modelMapper.map(theMarksCreateDTO, Marks.class);
        return marksRepository.save(marks);
    }

    @Override
    public void saveMarksList(List<MarksCreateDTO> marksCreateDTOList) {
        List<Marks> marksList = marksCreateDTOList.stream()
                .map(dto -> modelMapper.map(dto, Marks.class))
                .toList();
        marksRepository.saveAll(marksList);
    }

    @Override
    public void deleteById(int theId) {
        marksRepository.deleteById(theId);
    }
}
