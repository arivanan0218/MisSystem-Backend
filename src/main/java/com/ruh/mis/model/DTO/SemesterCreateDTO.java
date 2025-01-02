package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemesterCreateDTO {
    private int intakeId;
    private String semesterName;
    private String semesterYear;
    private String semesterDuration;
}
