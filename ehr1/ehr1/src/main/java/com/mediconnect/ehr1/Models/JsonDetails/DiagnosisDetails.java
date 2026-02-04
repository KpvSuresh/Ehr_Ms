package com.mediconnect.ehr1.Models.JsonDetails;

import java.time.LocalDate;

public class DiagnosisDetails {
    private String diagnosisName;
    private String icdCode;
    private String severity;
    private LocalDate onsetDate;
    private String notes;

    // --- 1. CONSTRUCTORS ---

    // Default Constructor (Required for JSON parsing)
    public DiagnosisDetails() {
    }

    // All-Args Constructor
    public DiagnosisDetails(String diagnosisName, String icdCode, String severity, LocalDate onsetDate, String notes) {
        this.diagnosisName = diagnosisName;
        this.icdCode = icdCode;
        this.severity = severity;
        this.onsetDate = onsetDate;
        this.notes = notes;
    }

    // --- 2. GETTERS AND SETTERS ---

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public LocalDate getOnsetDate() {
        return onsetDate;
    }

    public void setOnsetDate(LocalDate onsetDate) {
        this.onsetDate = onsetDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}