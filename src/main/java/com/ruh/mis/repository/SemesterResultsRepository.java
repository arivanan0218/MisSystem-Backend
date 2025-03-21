package com.ruh.mis.repository;

import com.ruh.mis.model.SemesterResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterResultsRepository extends JpaRepository<SemesterResults, Integer> {

    @Query("SELECT sr FROM SemesterResults sr " +
            "WHERE sr.department.id = :departmentId " +
            "AND sr.intake.id = :intakeId " +
            "AND sr.semester.id = :semesterId " +
            "AND sr.student.id = :studentId")
    Optional<SemesterResults> findByDepartmentIntakeSemesterAndStudent(
            @Param("departmentId") int departmentId,
            @Param("intakeId") int intakeId,
            @Param("semesterId") int semesterId,
            @Param("studentId") int studentId
    );

    @Query("SELECT sr FROM SemesterResults sr " +
            "WHERE sr.department.id = :departmentId " +
            "AND sr.intake.id = :intakeId " +
            "AND sr.semester.id = :semesterId")
    List<SemesterResults> findByDepartmentIntakeAndSemester(
            @Param("departmentId") int departmentId,
            @Param("intakeId") int intakeId,
            @Param("semesterId") int semesterId
    );
    
    @Query("SELECT sr FROM SemesterResults sr WHERE sr.student.id = :studentId")
    List<SemesterResults> findByStudent(@Param("studentId") int studentId);
}
