package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String moduleName;
    private String moduleCode;
    private int credit;
    private String GPA_Status;
    private String moduleCoordinator;


    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

//    @Version
//    private Integer version;
}
