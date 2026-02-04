package com.mediconnect.ehr1.dto;

import java.time.LocalDate;

public class PatientSummaryDto {

    // --- Fields ---
    private long id;
    private Long patientId;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodType;
    private String status;
    private String primaryDiagnosis;
    private LocalDate clinicalLastUpdated;

    // --- Constructors ---

    // No-args constructor (needed for libraries like Jackson/Hibernate)
    public PatientSummaryDto() {
    }

    // All-args constructor (useful for easy creation in tests or service layer)
    public PatientSummaryDto(Long patientId, String fullName, String gender, LocalDate dateOfBirth,
                             String bloodType, String status, String primaryDiagnosis, LocalDate clinicalLastUpdated) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.status = status;
        this.primaryDiagnosis = primaryDiagnosis;
        this.clinicalLastUpdated = clinicalLastUpdated;
    }

    // --- Getters and Setters ---

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrimaryDiagnosis() {
        return primaryDiagnosis;
    }

    public void setPrimaryDiagnosis(String primaryDiagnosis) {
        this.primaryDiagnosis = primaryDiagnosis;
    }

    public LocalDate getClinicalLastUpdated() {
        return clinicalLastUpdated;
    }

    public void setClinicalLastUpdated(LocalDate clinicalLastUpdated) {
        this.clinicalLastUpdated = clinicalLastUpdated;
    }
}