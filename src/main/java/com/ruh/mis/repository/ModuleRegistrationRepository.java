package com.ruh.mis.repository;

import com.ruh.mis.model.ModuleRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRegistrationRepository extends JpaRepository<ModuleRegistration, Integer> {
}
