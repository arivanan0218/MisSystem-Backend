package com.ruh.mis.model;

/**
 * Enum representing types of modules in the curriculum
 * CM: Core Module - Mandatory modules that are always GPA counted
 * TE: Technical Elective - Optional technical modules where students can choose GPA status
 * GE: General Elective - Non-technical optional modules that are always non-GPA
 */
public enum ModuleType {
    CM, // Core Module
    TE, // Technical Elective
    GE  // General Elective
}