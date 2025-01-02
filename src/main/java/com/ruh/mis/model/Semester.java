package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "semesters")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String semesterName;
    private String semesterYear;
    private String semesterDuration;


    @ManyToOne
    @JoinColumn(name = "intake_id")
    private Intake intake;

//    @Version
//    private Integer version;
}
