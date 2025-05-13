package com.ruh.mis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ruh.mis.model.Lecturer;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    List<Lecturer> findByDepartment_Id(int departmentId);
}