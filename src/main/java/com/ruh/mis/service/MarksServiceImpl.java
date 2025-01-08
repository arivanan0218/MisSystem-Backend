package com.ruh.mis.service;

import com.ruh.mis.model.DTO.MarksCreateDTO;
import com.ruh.mis.model.DTO.MarksDTO;
import com.ruh.mis.model.Marks;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.MarksRepository;
import com.ruh.mis.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarksServiceImpl implements MarksService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository; // Assuming you have a Student repository

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<MarksDTO> findAll() {
        return marksRepository.findAll().stream()
                .map(marks -> {
                    // Map Marks entity to MarksDTO
                    MarksDTO marksDTO = modelMapper.map(marks, MarksDTO.class);

                    // Extract student ID and name from the associated students (single student)
                    if (!marks.getStudents().isEmpty()) {
                        Student student = marks.getStudents().get(0); // Assuming one student per Marks entry
                        marksDTO.setStudentId(student.getId());
                        marksDTO.setStudentName(student.getStudent_name()); // Correct getter for student name
                    }

                    return marksDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MarksDTO findById(int theId) {
        Marks marks = marksRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Marks not found: " + theId));

        // Map Marks entity to MarksDTO
        MarksDTO marksDTO = modelMapper.map(marks, MarksDTO.class);

        // Extract student ID and name from the associated students (single student)
        if (!marks.getStudents().isEmpty()) {
            Student student = marks.getStudents().get(0); // Assuming one student per Marks entry
            marksDTO.setStudentId(student.getId());
            marksDTO.setStudentName(student.getStudent_name()); // Correct getter for student name
        }

        return marksDTO;
    }

    @Override
    public List<MarksDTO> getAssignmentByDepartmentIdAndIntakeIdAndSemesterIdAndModuleAndAssignmentId(int departmentId, int intakeId, int semesterId, int moduleId, int assignmentId) {
        List<Marks> marksList = marksRepository.findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndAssignmentId(departmentId, intakeId, semesterId, moduleId, assignmentId);

        return marksList.stream()
                .map(marks -> {
                    // Map Marks entity to MarksDTO
                    MarksDTO marksDTO = modelMapper.map(marks, MarksDTO.class);

                    // Extract student ID and name from the associated students (single student)
                    if (!marks.getStudents().isEmpty()) {
                        Student student = marks.getStudents().get(0); // Assuming one student per Marks entry
                        marksDTO.setStudentId(student.getId());
                        marksDTO.setStudentName(student.getStudent_name()); // Correct getter for student name
                    }

                    return marksDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Marks save(MarksCreateDTO marksCreateDTO) {
        // Map MarksCreateDTO to Marks entity
        Marks marks = modelMapper.map(marksCreateDTO, Marks.class);

        // Retrieve the student by ID and associate it with the Marks entity
        Student student = studentRepository.findById(marksCreateDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + marksCreateDTO.getStudentId()));

        marks.setStudents(List.of(student)); // Assuming only one student per Marks entry

        // Save and return the Marks entity
        return marksRepository.save(marks);
    }

    @Override
    public void saveMarksList(List<MarksCreateDTO> marksCreateDTOList) {
        List<Marks> marksList = marksCreateDTOList.stream()
                .map(dto -> {
                    // Map MarksCreateDTO to Marks entity
                    Marks marks = modelMapper.map(dto, Marks.class);

                    // Retrieve the student by ID and associate it with the Marks entity
                    Student student = studentRepository.findById(dto.getStudentId())
                            .orElseThrow(() -> new RuntimeException("Student not found: " + dto.getStudentId()));

                    marks.setStudents(List.of(student)); // Assuming only one student per Marks entry
                    return marks;
                })
                .collect(Collectors.toList());

        // Save all Marks entities
        marksRepository.saveAll(marksList);
    }

    @Override
    public void deleteById(int theId) {
        marksRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public MarksDTO update(int marksId, MarksCreateDTO marksCreateDTO) {
        // Find the existing Marks entity
        Marks existingMarks = marksRepository.findById(marksId)
                .orElseThrow(() -> new RuntimeException("Marks not found: " + marksId));

        // Update the marks obtained
        existingMarks.setMarksObtained(marksCreateDTO.getObtainedMarks());

        // Retrieve the student by ID and associate it with the Marks entity
        Student student = studentRepository.findById(marksCreateDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + marksCreateDTO.getStudentId()));

        existingMarks.setStudents(List.of(student)); // Assuming only one student per Marks entry

        // Save the updated Marks entity
        Marks updatedMarks = marksRepository.save(existingMarks);

        // Return updated MarksDTO
        return modelMapper.map(updatedMarks, MarksDTO.class);
    }
}
