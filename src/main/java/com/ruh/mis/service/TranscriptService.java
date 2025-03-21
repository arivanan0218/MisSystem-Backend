package com.ruh.mis.service;

import com.ruh.mis.model.DTO.TranscriptDTO;

public interface TranscriptService {
    /**
     * Generate a transcript for a student
     * 
     * @param studentId The ID of the student
     * @return The transcript DTO containing all student and module information
     */
    TranscriptDTO generateTranscript(int studentId);
}
