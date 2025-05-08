package com.ruh.mis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ruh.mis.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT s FROM Student s WHERE s.department.id = :departmentId AND s.intake.id = :intakeId")
    List<Student> findByDepartmentIdAndIntakeId(@Param("departmentId") int departmentId, @Param("intakeId") int intakeId);
    
    @Query("SELECT s FROM Student s WHERE s.department.id = :departmentId AND s.intake.id = :intakeId")
    List<Student> findByDepartmentAndIntake(@Param("departmentId") int departmentId, @Param("intakeId") int intakeId);
}
