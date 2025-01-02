package com.ruh.mis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {
    private int id;
    private int moduleId;
    private String assignmentName;
    private String assignmentDuration;

}
