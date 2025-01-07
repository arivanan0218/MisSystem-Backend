package com.ruh.mis.repository;

import com.ruh.mis.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query("SELECT a FROM Assignment a " +
            "JOIN a.module m " +
            "JOIN m.semester s " +
            "JOIN s.intake i " +
            "JOIN i.department d " +
            "WHERE d.id = :departmentId " +
            "AND i.id = :intakeId " +
            "AND s.id = :semesterId " +
            "AND m.id = :moduleId")
    List<Assignment> findByDepartmentIdAndIntakeIdAndSemesterIdAndModuleId(@Param("departmentId") int departmentId,
                                                                           @Param("intakeId") int intakeId,
                                                                           @Param("semesterId") int semesterId,
                                                                           @Param("moduleId") int moduleId);
}
