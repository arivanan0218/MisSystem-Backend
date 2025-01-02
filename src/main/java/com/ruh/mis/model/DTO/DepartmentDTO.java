package com.ruh.mis.model.DTO;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private Long id;
    private String departmentName;
    private String departmentCode;
}
