package com.ruh.mis.repository;

import com.ruh.mis.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Integer> {
}
