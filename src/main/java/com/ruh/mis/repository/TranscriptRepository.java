package com.ruh.mis.repository;

import com.ruh.mis.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {
    
    @Query("SELECT t FROM Transcript t WHERE t.student.id = :studentId")
    Optional<Transcript> findByStudentId(@Param("studentId") int studentId);
    
    @Query("SELECT t FROM Transcript t WHERE t.department.id = :departmentId AND t.intake.id = :intakeId AND t.student.id = :studentId")
    Optional<Transcript> findByDepartmentIntakeAndStudent(
            @Param("departmentId") int departmentId,
            @Param("intakeId") int intakeId,
            @Param("studentId") int studentId);
}
