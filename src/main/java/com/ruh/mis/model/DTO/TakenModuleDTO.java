package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakenModuleDTO {
    private int moduleId;
    private String gpaStatus;  // GPA status for the module (e.g., "G", "N")
}
