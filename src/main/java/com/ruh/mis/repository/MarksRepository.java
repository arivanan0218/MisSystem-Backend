package com.ruh.mis.repository;

import com.ruh.mis.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Integer> {
    List<Marks> findByStudentIdAndAssignmentId(int studentId, int assignmentId);
    List<Marks> findByStudentId(int studentId);
    List<Marks> findByAssignment_Id(int assignmentId);
}
