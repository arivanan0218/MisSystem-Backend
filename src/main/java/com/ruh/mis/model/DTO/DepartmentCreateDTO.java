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
    private String hodName;
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public String getDepartmentCode() {
        return departmentCode;
    }
    
    public String getHodName() {
        return hodName;
    }
}
