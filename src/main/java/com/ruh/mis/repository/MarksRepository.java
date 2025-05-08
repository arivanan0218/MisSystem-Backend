package com.ruh.mis.repository;

import com.ruh.mis.model.Assignment;
import com.ruh.mis.model.Marks;
import com.ruh.mis.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Integer> {
    List<Marks> findByStudentIdAndAssignmentId(int studentId, int assignmentId);
    List<Marks> findByStudentId(int studentId);
    List<Marks> findByAssignment_Id(int assignmentId);
    
    /**
     * Find all distinct students who have marks for any of the given assignments
     */
    @Query("SELECT DISTINCT m.student FROM Marks m WHERE m.assignment IN :assignments")
    List<Student> findDistinctStudentsByAssignmentIn(@Param("assignments") List<Assignment> assignments);
}
