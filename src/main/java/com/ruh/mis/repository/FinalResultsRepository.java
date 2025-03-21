package com.ruh.mis.repository;

import com.ruh.mis.model.FinalResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinalResultsRepository extends JpaRepository<FinalResults, Integer> {

    @Query("SELECT fr FROM FinalResults fr WHERE fr.department.id = :departmentId AND fr.intake.id = :intakeId")
    List<FinalResults> findByDepartmentAndIntake(@Param("departmentId") int departmentId, @Param("intakeId") int intakeId);

    @Query("SELECT fr FROM FinalResults fr WHERE fr.department.id = :departmentId AND fr.intake.id = :intakeId AND fr.student.id = :studentId")
    Optional<FinalResults> findByDepartmentIntakeAndStudent(
            @Param("departmentId") int departmentId,
            @Param("intakeId") int intakeId,
            @Param("studentId") int studentId);
            
    @Query("SELECT fr FROM FinalResults fr WHERE fr.student.id = :studentId")
    Optional<FinalResults> findByStudentId(@Param("studentId") int studentId);
}
