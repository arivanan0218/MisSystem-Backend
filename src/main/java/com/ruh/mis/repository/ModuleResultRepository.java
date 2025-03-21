package com.ruh.mis.repository;

import com.ruh.mis.model.ModuleResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleResultRepository extends JpaRepository<ModuleResult, Integer> {

    @Query("SELECT mr FROM ModuleResult mr " +
            "WHERE mr.department.id = :departmentId " +
            "AND mr.intake.id = :intakeId " +
            "AND mr.semester.id = :semesterId " +
            "AND mr.module.id = :moduleId " +
            "AND mr.student.id = :studentId")
    Optional<ModuleResult> findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndStudentId(
            @Param("departmentId") int departmentId,
            @Param("intakeId") int intakeId,
            @Param("semesterId") int semesterId,
            @Param("moduleId") int moduleId,
            @Param("studentId") int studentId
    );

    @Query("SELECT mr FROM ModuleResult mr " +
            "JOIN FETCH mr.department " +
            "JOIN FETCH mr.intake " +
            "JOIN FETCH mr.semester " +
            "JOIN FETCH mr.module " +
            "JOIN FETCH mr.student " +
            "WHERE mr.department.id = :departmentId " +
            "AND mr.intake.id = :intakeId " +
            "AND mr.semester.id = :semesterId " +
            "AND mr.module.id = :moduleId")
    List<ModuleResult> findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
            @Param("departmentId") int departmentId,
            @Param("intakeId") int intakeId,
            @Param("semesterId") int semesterId,
            @Param("moduleId") int moduleId
    );
    
    @Query("SELECT mr FROM ModuleResult mr " +
            "JOIN FETCH mr.department " +
            "JOIN FETCH mr.intake " +
            "JOIN FETCH mr.semester " +
            "JOIN FETCH mr.module " +
            "JOIN FETCH mr.student " +
            "WHERE mr.student.id = :studentId")
    List<ModuleResult> findByStudentId(@Param("studentId") int studentId);
}
