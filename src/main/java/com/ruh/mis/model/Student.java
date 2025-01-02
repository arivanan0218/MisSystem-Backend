package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String student_name;
    private String student_Reg_No;
    private String student_email;
    private String student_NIC;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

//    @Version
//    private Integer version;
}
