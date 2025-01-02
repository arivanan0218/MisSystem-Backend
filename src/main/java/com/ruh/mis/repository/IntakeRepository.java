package com.ruh.mis.repository;

import com.ruh.mis.model.Intake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntakeRepository extends JpaRepository<Intake, Integer> {
    @Query("SELECT i FROM Intake i WHERE i.department.id = :departmentId")
    List<Intake> findByDepartmentId(int departmentId);
}
