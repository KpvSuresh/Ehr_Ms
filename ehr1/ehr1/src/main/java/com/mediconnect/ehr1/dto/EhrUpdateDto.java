package com.mediconnect.ehr1.dto;

import com.mediconnect.ehr1.Models.EhrRecordType;
import java.util.Map;

public class EhrUpdateDto {

    private Long recordId;            // Optional: Null = Create New, ID = Update Existing
    private Long patientId;           // REQUIRED
    private Long doctorId;            // REQUIRED
    private EhrRecordType recordType; // REQUIRED: DIAGNOSIS, LAB_RESULT, or NOTE
    private Map<String, Object> data; // The actual content

    // --- 1. CONSTRUCTORS ---

    // Default Constructor (Required for JSON parsing)
    public EhrUpdateDto() {
    }

    // All-Args Constructor
    public EhrUpdateDto(Long recordId, Long patientId, Long doctorId, EhrRecordType recordType, Map<String, Object> data) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.recordType = recordType;
        this.data = data;
    }

    // --- 2. GETTERS AND SETTERS ---

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

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

    public EhrRecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(EhrRecordType recordType) {
        this.recordType = recordType;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}