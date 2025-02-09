package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String departmentName;
    private String departmentCode;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Student> students;
}
