package com.mediconnect.ehr1.dto;

import com.mediconnect.ehr1.Models.JsonDetails.DiagnosisDetails;
import com.mediconnect.ehr1.Models.JsonDetails.LabResultDetails;
import com.mediconnect.ehr1.Models.JsonDetails.NoteDetails;

import java.time.LocalDate;
import java.util.List;

public class PatientEhrDataDto {
    // Basic Info
    private Long patientId;
    private String fullName;
    private String gender;
    private LocalDate dob;
//    private String bloodGroup;

    // Clinical Records
    private List<DiagnosisDetails> diagnoses;
    private List<LabResultDetails> labResults;
    // private List<NoteDetails> doctorNotes;

    // --- NEW: Audit Log ---
    private List<ActivityLogDto> auditHistory;

    // --- 1. CONSTRUCTORS ---

    public PatientEhrDataDto() {
    }

    // --- 2. GETTERS AND SETTERS ---

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    //    public String getBloodGroup() { return bloodGroup; }
//    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public List<DiagnosisDetails> getDiagnoses() { return diagnoses; }
    public void setDiagnoses(List<DiagnosisDetails> diagnoses) { this.diagnoses = diagnoses; }

    public List<LabResultDetails> getLabResults() { return labResults; }
    public void setLabResults(List<LabResultDetails> labResults) { this.labResults = labResults; }

    // public List<NoteDetails> getDoctorNotes() { return doctorNotes; }
    // public void setDoctorNotes(List<NoteDetails> doctorNotes) { this.doctorNotes = doctorNotes; }

    // --- NEW Getter/Setter for Audit ---
    public List<ActivityLogDto> getAuditHistory() { return auditHistory; }
    public void setAuditHistory(List<ActivityLogDto> auditHistory) { this.auditHistory = auditHistory; }
}