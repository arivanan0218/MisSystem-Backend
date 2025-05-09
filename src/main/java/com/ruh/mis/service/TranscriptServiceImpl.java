package com.ruh.mis.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruh.mis.model.DTO.TranscriptDTO;
import com.ruh.mis.model.FinalResults;
import com.ruh.mis.model.ModuleResult;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.SemesterResults;
import com.ruh.mis.model.Student;
import com.ruh.mis.repository.FinalResultsRepository;
import com.ruh.mis.repository.ModuleResultRepository;
import com.ruh.mis.repository.SemesterResultsRepository;
import com.ruh.mis.repository.StudentRepository;

@Service
public class TranscriptServiceImpl implements TranscriptService {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ModuleResultRepository moduleResultRepository;
    
    @Autowired
    private SemesterResultsRepository semesterResultsRepository;
    
    @Autowired
    private FinalResultsRepository finalResultsRepository;
    
    // Removed unused variables

    @Override
    public TranscriptDTO generateTranscript(int studentId) {
        // Get the student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        
        // Get all module results for the student
        List<ModuleResult> moduleResults = moduleResultRepository.findByStudentId(studentId);
        
        // Get all semester results for the student
        List<SemesterResults> semesterResults = semesterResultsRepository.findByStudent(studentId);
        
        // Get final results for the student if available
        Optional<FinalResults> finalResultsOpt = finalResultsRepository.findByStudentId(studentId);
        
        // Create the transcript DTO
        TranscriptDTO transcriptDTO = new TranscriptDTO();
        
        // Set student information
        transcriptDTO.setStudentId(student.getId());
        transcriptDTO.setStudentName(student.getStudentName());
        transcriptDTO.setStudentRegNo(student.getStudentRegNo());
        
        // Set department and intake information
        transcriptDTO.setDepartmentId(student.getDepartment().getId());
        transcriptDTO.setDepartmentName(student.getDepartment().getDepartmentName());
        transcriptDTO.setIntakeId(student.getIntake().getId());
        transcriptDTO.setIntakeName(student.getIntake().getIntakeYear() + " - " + student.getIntake().getBatch());
        
        // Set overall GPA and total credits
        double overallGPA = finalResultsOpt.map(FinalResults::getOverallGpa).orElse(0.0);
        transcriptDTO.setOverallGpa(overallGPA);
        
        // Calculate total credits
        int totalCredits = moduleResults.stream()
                .map(result -> result.getModule().getCredit())
                .reduce(0, Integer::sum);
        transcriptDTO.setTotalCredits(totalCredits);
        
        // Group module results by semester
        Map<Integer, List<ModuleResult>> moduleResultsBySemester = moduleResults.stream()
                .collect(Collectors.groupingBy(result -> result.getSemester().getId()));
        
        // Create semester transcript DTOs
        List<TranscriptDTO.SemesterTranscriptDTO> semesterDTOs = new ArrayList<>();
        
        for (SemesterResults semResult : semesterResults) {
            Semester semester = semResult.getSemester();
            int semesterId = semester.getId();
            
            TranscriptDTO.SemesterTranscriptDTO semesterDTO = new TranscriptDTO.SemesterTranscriptDTO();
            semesterDTO.setSemesterId(semesterId);
            semesterDTO.setSemesterName(semester.getSemesterName());
            semesterDTO.setSemesterGPA(semResult.getSemesterGPA());
            
            // Get module results for this semester
            List<ModuleResult> semesterModuleResults = moduleResultsBySemester.getOrDefault(semesterId, Collections.emptyList());
            
            // Create module transcript DTOs
            List<TranscriptDTO.ModuleTranscriptDTO> moduleDTOs = new ArrayList<>();
            
            for (ModuleResult moduleResult : semesterModuleResults) {
                com.ruh.mis.model.Module module = moduleResult.getModule();
                
                TranscriptDTO.ModuleTranscriptDTO moduleDTO = new TranscriptDTO.ModuleTranscriptDTO();
                moduleDTO.setModuleId(module.getId());
                moduleDTO.setModuleCode(module.getModuleCode());
                moduleDTO.setModuleName(module.getModuleName());
                moduleDTO.setModuleCredits(module.getCredit());
                moduleDTO.setModuleGrade(moduleResult.getGrade());
                moduleDTO.setModuleGradePoint(moduleResult.getGradePoint());
                
                moduleDTOs.add(moduleDTO);
            }
            
            // Sort modules by module code
            moduleDTOs.sort(Comparator.comparing(TranscriptDTO.ModuleTranscriptDTO::getModuleCode));
            
            semesterDTO.setModules(moduleDTOs);
            semesterDTOs.add(semesterDTO);
        }
        
        // Sort semesters by semester ID
        semesterDTOs.sort(Comparator.comparing(TranscriptDTO.SemesterTranscriptDTO::getSemesterId));
        
        transcriptDTO.setSemesters(semesterDTOs);
        
        return transcriptDTO;
    }
}
