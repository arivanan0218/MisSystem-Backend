package com.ruh.mis.repository;

import com.ruh.mis.model.ModuleRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRegistrationRepository extends JpaRepository<ModuleRegistration, Integer> {
    List<ModuleRegistration> findByStudentIdAndSemesterIdAndIntakeIdAndDepartmentId(int studentId, int semesterId, int intakeId, int departmentId);
}
