package com.ruh.mis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;



//model for endexam
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "end_exams", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"module_id", "semester_id", "intake_id", "department_id"})
})
public class EndExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String endExamName;
    private int endExamPercentage;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "intake_id")
    private Intake intake;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToMany
    @JoinTable(
            name = "end_exam_students",
            joinColumns = @JoinColumn(name = "end_exam_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
}