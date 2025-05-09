package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerCreateDTO {
    private int departmentId;
    private String lecturerName;
    private String lecturerEmail;
    private String lecturerPhoneNumber;
    private String username;
    private String password;
}
