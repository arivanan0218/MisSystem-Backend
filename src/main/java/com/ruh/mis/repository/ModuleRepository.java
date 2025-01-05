package com.ruh.mis.repository;

import com.ruh.mis.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    @Query("SELECT m FROM Module m " +
            "JOIN m.semester s " +
            "JOIN s.intake i " +
            "JOIN s.department d " +
            "WHERE d.id = :departmentId " +
            "AND i.id = :intakeId " +
            "AND s.id = :semesterId")
    List<Module> findByDepartmentIdAndIntakeIdAndSemesterId(@Param("departmentId") int departmentId,
                                                            @Param("intakeId") int intakeId,
                                                            @Param("semesterId") int semesterId);


}
