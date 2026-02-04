package com.mediconnect.ehr1.dto;

import com.mediconnect.ehr1.Models.PatientStatus;

public class UpdateStatusDto {
    private Long patientId;
    private Long doctorId;
    private PatientStatus patientStatus;

    // --- 1. CONSTRUCTORS ---

    // Default Constructor (Critical for Jackson JSON parsing)
    public UpdateStatusDto() {
    }

    // All-Args Constructor (Useful for manual creation)
    public UpdateStatusDto(Long patientId, Long doctorId, PatientStatus patientStatus) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientStatus = patientStatus;
    }

    // --- 2. GETTERS AND SETTERS ---

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public PatientStatus getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(PatientStatus patientStatus) {
        this.patientStatus = patientStatus;
    }
}