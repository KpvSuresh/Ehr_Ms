package com.mediconnect.ehr1.Models;

import com.Team2.Mediconnect.m1.Models.Patient;
import com.Team2.Mediconnect.m2.Models.Doctor;
import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode; // Corrected import for standard Jackson
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Entity
@Table(name = "ehr_records")
public class EhrRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EhrRecordType ehrRecordType;

    // --- NEW SIMPLIFIED OUTCOME ---
    @Enumerated(EnumType.STRING)
    @Column(name = "outcome")
    private TreatmentOutcome outcome;

    @Enumerated(EnumType.STRING)
    @Column(name = "patient_status")
    private PatientStatus patientStatus;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt;

    private static final ObjectMapper mapper = new ObjectMapper();

    // --- AUTOMATIC DEFAULTS ---
    @PrePersist
    protected void onCreate() {
        // 1. Set Creation Date
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

        // 2. Default Outcome to 'ON_TREATMENT' (Simplicity logic)
        if (this.outcome == null) {
            this.outcome = TreatmentOutcome.ON_TREATMENT;
        }
    }

    // --- GETTERS AND SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public EhrRecordType getEhrRecordType() { return ehrRecordType; }
    public void setEhrRecordType(EhrRecordType ehrRecordType) { this.ehrRecordType = ehrRecordType; }

    // ** Added missing Getter/Setter for Outcome **
    public TreatmentOutcome getOutcome() { return outcome; }
    public void setOutcome(TreatmentOutcome outcome) { this.outcome = outcome; }

    public PatientStatus getPatientStatus() { return patientStatus; }
    public void setPatientStatus(PatientStatus patientStatus) { this.patientStatus = patientStatus; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // --- DASHBOARD SUMMARY LOGIC ---
    @Transient
    public String getDashboardSummary() {
        if (this.ehrRecordType == null) return "N/A";

        if (this.ehrRecordType == EhrRecordType.DIAGNOSIS) {
            try {
                JsonNode root = mapper.readTree(this.description);
                return root.path("diagnosisName").asText("Unknown Diagnosis");
            } catch (Exception e) {
                return "Data Error";
            }
        }
        return this.description;
    }
}

// package com.Team2.Mediconnect.m3.Models;

// import com.Team2.Mediconnect.m1.Models.Patient;
// import com.Team2.Mediconnect.m2.Models.Doctor;
// import jakarta.persistence.*;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "ehr_records")
// public class EhrRecord {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne
//     @JoinColumn(name = "patient_id", nullable = false)
//     private Patient patient;

//     @ManyToOne
//     @JoinColumn(name = "doctor_id", nullable = false)
//     private Doctor doctor;

//     @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//     private EhrRecordType ehrRecordType;

//     @Enumerated(EnumType.STRING)
//     @Column(name = "outcome")
//     private TreatmentOutcome outcome;

//     @Enumerated(EnumType.STRING)
//     @Column(name = "patient_status")
//     private PatientStatus patientStatus;

//     @Column(columnDefinition = "TEXT")
//     private String description;

//     private LocalDateTime createdAt;
//     private LocalDateTime updatedAt; // Track latest changes

//     private static final ObjectMapper mapper = new ObjectMapper();

//     @PrePersist
//     protected void onCreate() {
//         this.createdAt = LocalDateTime.now();
//         this.updatedAt = LocalDateTime.now();
//         if (this.outcome == null) {
//             this.outcome = TreatmentOutcome.ON_TREATMENT;
//         }
//     }

//     @PreUpdate
//     protected void onUpdate() {
//         this.updatedAt = LocalDateTime.now(); // This updates the timestamp on every save
//     }

//     // --- GETTERS AND SETTERS ---
//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }
//     public Patient getPatient() { return patient; }
//     public void setPatient(Patient patient) { this.patient = patient; }
//     public Doctor getDoctor() { return doctor; }
//     public void setDoctor(Doctor doctor) { this.doctor = doctor; }
//     public EhrRecordType getEhrRecordType() { return ehrRecordType; }
//     public void setEhrRecordType(EhrRecordType ehrRecordType) { this.ehrRecordType = ehrRecordType; }
//     public TreatmentOutcome getOutcome() { return outcome; }
//     public void setOutcome(TreatmentOutcome outcome) { this.outcome = outcome; }
//     public PatientStatus getPatientStatus() { return patientStatus; }
//     public void setPatientStatus(PatientStatus patientStatus) { this.patientStatus = patientStatus; }
//     public String getDescription() { return description; }
//     public void setDescription(String description) { this.description = description; }
//     public LocalDateTime getCreatedAt() { return createdAt; }
//     public LocalDateTime getUpdatedAt() { return updatedAt; }

//     @Transient
//     public String getDashboardSummary() {
//         if (this.ehrRecordType == EhrRecordType.DIAGNOSIS && this.description != null) {
//             try {
//                 JsonNode root = mapper.readTree(this.description);
//                 return root.path("diagnosisName").asText("Unknown Diagnosis");
//             } catch (Exception e) { return "Data Error"; }
//         }
//         return this.description;
//     }
// }