package com.ruh.mis.repository;

import com.ruh.mis.model.ModuleRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRegistrationRepository extends JpaRepository<ModuleRegistration, Integer> {

    List<ModuleRegistration> findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentId(
            int studentId, int semesterId, int intakeId, int departmentId);

    @Query("SELECT mr FROM ModuleRegistration mr " +
            "WHERE mr.student.id = :studentId " +
            "AND mr.semester.id = :semesterId " +
            "AND mr.intake.id = :intakeId " +
            "AND mr.department.id = :departmentId " +
            "AND mr.module.id = :moduleId")
    Optional<ModuleRegistration> findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentIdAndModuleId(
            int studentId, int semesterId, int intakeId, int departmentId, int moduleId);
}
