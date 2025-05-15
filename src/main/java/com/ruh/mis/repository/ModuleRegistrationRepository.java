package com.ruh.mis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ruh.mis.model.ModuleRegistration;

@Repository
public interface ModuleRegistrationRepository extends JpaRepository<ModuleRegistration, Integer> {

    // Find registrations by student, semester, intake, and department
    List<ModuleRegistration> findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentId(
            int studentId, int semesterId, int intakeId, int departmentId);

    // Find specific registration
    @Query("SELECT mr FROM ModuleRegistration mr " +
            "WHERE mr.student.id = :studentId " +
            "AND mr.semester.id = :semesterId " +
            "AND mr.intake.id = :intakeId " +
            "AND mr.department.id = :departmentId " +
            "AND mr.module.id = :moduleId")
    Optional<ModuleRegistration> findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(
            int studentId, int semesterId, int intakeId, int departmentId, int moduleId);
            
    // Find registrations by module ID
    List<ModuleRegistration> findByModuleId(int moduleId);
    
    // Find registrations by module ID, semester, intake, and department
    List<ModuleRegistration> findByModuleIdAndSemesterIdAndIntakeIdAndDepartmentId(
            int moduleId, int semesterId, int intakeId, int departmentId);
    
    // Find all pending registrations
    List<ModuleRegistration> findByRegistrationStatus(String registrationStatus);
    
    // Find pending registrations by department, intake, and semester
    List<ModuleRegistration> findByDepartmentIdAndIntakeIdAndSemesterIdAndRegistrationStatus(
            int departmentId, int intakeId, int semesterId, String registrationStatus);
}