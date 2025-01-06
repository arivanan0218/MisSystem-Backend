package com.ruh.mis.repository;

import com.ruh.mis.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Integer> {
    @Query("SELECT am FROM Marks am " +
    "JOIN am.assignment a " +
    "JOIN a.module m " +
    "JOIN m.semester s " +
    "JOIN s.intake i " +
    "JOIN i.department d " +
    "WHERE d.id = :departmentId " +
    "AND i.id = :intakeId " +
    "AND s.id = :semesterId " +
    "AND m.id = :moduleId " +
    "AND a.id = :assignmentId")
    List<Marks> findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleIdAndAssignmentId(@Param("departmentId") int departmentId,
                                                                                     @Param("intakeId") int intakeId,
                                                                                     @Param("semesterId") int semesterId,
                                                                                     @Param("moduleId") int moduleId,
                                                                                     @Param("assignmentId") int assignmentId);
}
