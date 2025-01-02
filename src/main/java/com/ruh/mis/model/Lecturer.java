package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lecturers")
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String phoneNumber;
    private String info;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

//    @Version
//    private Integer version;
}
