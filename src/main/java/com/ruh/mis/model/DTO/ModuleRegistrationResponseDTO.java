package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleRegistrationResponseDTO {
    private int id;
    private String studentName;
    private String studentRegNo;
    private String departmentName;
//    private List<String> modules;
    private List<Map<String, String>> modules;
}