package com.ruh.mis.repository;

import com.ruh.mis.model.EndExamMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndExamMarksRepository extends JpaRepository<EndExamMarks, Integer> {
    List<EndExamMarks> findByStudentId(int studentId);
    List<EndExamMarks> findByEndExamId(int endExamId);
}