package com.ruh.mis.repository;

import com.ruh.mis.model.EndExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndExamRepository extends JpaRepository<EndExam, Integer> {
    @Query("SELECT e FROM EndExam e " +
            "JOIN e.module m " +
            "JOIN m.semester s " +
            "JOIN s.intake i " +
            "JOIN i.department d " +
            "WHERE d.id = :departmentId " +
            "AND i.id = :intakeId " +
            "AND s.id = :semesterId " +
            "AND m.id = :moduleId")
    List<EndExam> findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(
            @Param("departmentId") int departmentId,
            @Param("intakeId") int intakeId,
            @Param("semesterId") int semesterId,
            @Param("moduleId") int moduleId
    );
}