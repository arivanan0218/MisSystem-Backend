package com.ruh.mis.repository;


import com.ruh.mis.model.Semester;
import com.ruh.mis.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
}