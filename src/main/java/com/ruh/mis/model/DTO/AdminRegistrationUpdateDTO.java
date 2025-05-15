package com.ruh.mis.model.DTO;

import com.ruh.mis.model.GPAStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegistrationUpdateDTO {
    private int registrationId;
    private String action; // "APPROVE", "REJECT", "EDIT"
    private GPAStatus gpaStatus; // Only needed for EDIT
    private String status; // Only needed for EDIT: "Taken", "Not-Taken"
}