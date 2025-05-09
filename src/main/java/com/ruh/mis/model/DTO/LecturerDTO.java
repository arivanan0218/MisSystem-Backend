package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerDTO {
    private int id;
    private int departmentId;
    private String lecturerName;
    private String lecturerEmail;
    private String lecturerPhoneNumber;
    private String username;
    private String password;
}
