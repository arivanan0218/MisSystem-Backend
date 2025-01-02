package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateDTO {
    private String departmentName;
    private String departmentCode;
}
