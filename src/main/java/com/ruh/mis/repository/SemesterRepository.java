package com.ruh.mis.repository;


import com.ruh.mis.model.Intake;
import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    @Query("SELECT s FROM Semester s WHERE s.intake.department.id = :departmentId AND s.intake.id = :intakeId")
    List<Semester> findByDepartmentIdAndIntakeId(@Param("departmentId") int departmentId,
                                                 @Param("intakeId") int intakeId);
}