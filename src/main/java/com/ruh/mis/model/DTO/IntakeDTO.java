package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntakeDTO {
    private int id;
    private int departmentId;
    private String intakeYear;
    private String batch;
}