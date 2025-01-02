package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateDTO {
    private int departmentId;
    private String student_name;
    private String student_Reg_No;
    private String student_email;
    private String student_NIC;
}
