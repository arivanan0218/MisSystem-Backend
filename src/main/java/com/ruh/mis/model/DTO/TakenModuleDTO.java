package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakenModuleDTO {
    private int moduleId;
    private String gpaStatus; // "G" for GPA, "N" for Non-GPA, "-" for Not Taken
    private String moduleType; // "CM", "TE", "GE"
}