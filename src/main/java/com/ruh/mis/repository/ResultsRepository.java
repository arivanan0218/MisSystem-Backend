package com.ruh.mis.repository;

import com.ruh.mis.model.Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultsRepository extends JpaRepository<Results, Integer> {
}
