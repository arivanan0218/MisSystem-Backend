package com.ruh.mis.repository;

import com.ruh.mis.model.ModuleRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRegistrationRepository extends JpaRepository<ModuleRegistration, Integer> {
    @Query("SELECT m FROM ModuleRegistration m JOIN m.students s WHERE s.id = :studentId")
    List<ModuleRegistration> findBYStudentId(int studentId);
}
