package com.mediconnect.ehr1.Services;

import com.Team2.Mediconnect.m1.Models.Patient;
import com.Team2.Mediconnect.m1.Repository.PatientRepository;
import com.Team2.Mediconnect.m2.Models.Doctor;
import com.Team2.Mediconnect.m2.Repository.AppointmentRepository;
import com.Team2.Mediconnect.m2.Repository.DoctorRepository;
import com.mediconnect.ehr1.Models.EhrRecord;
import com.mediconnect.ehr1.Models.EhrRecordType;
import com.mediconnect.ehr1.Models.JsonDetails.AuditDetails;
import com.mediconnect.ehr1.Models.JsonDetails.DiagnosisDetails;
import com.mediconnect.ehr1.Models.JsonDetails.LabResultDetails;
//import com.Team2.Mediconnect.m3.Models.JsonDetails.NoteDetails;
import com.mediconnect.ehr1.Models.PatientStatus;
import com.mediconnect.ehr1.Repository.EhrRecordRepository;
import com.mediconnect.ehr1.m3.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EhrService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EhrRecordRepository ehrRecordRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * MAIN DASHBOARD METHOD
     */
    public List<PatientSummaryDto> getDoctorDashboard(Long doctorId) {
        List<Patient> myPatients = appointmentRepository.findPatientsByDoctorId(doctorId);
        return myPatients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PatientSummaryDto convertToDto(Patient patient) {
        PatientSummaryDto dto = new PatientSummaryDto();

        // 1. Basic Info
        dto.setPatientId(patient.getId());
        dto.setFullName(patient.getFullName());
        dto.setGender(patient.getGender().name());
        dto.setDateOfBirth(patient.getDob());

        // -----------------------------------------------------------
        // FIX PART A: GET REAL-TIME STATUS (From Latest Record)
        // -----------------------------------------------------------
        // We use the new strict method to find the absolute newest row in the DB.
        Optional<EhrRecord> latestAnyRecord = ehrRecordRepository
                .findFirstByPatientIdOrderByCreatedAtDesc(patient.getId());

        if (latestAnyRecord.isPresent() && latestAnyRecord.get().getPatientStatus() != null) {
            // Success: We found the actual status (e.g., "INPATIENT" or "DISCHARGED")
            dto.setStatus(latestAnyRecord.get().getPatientStatus().name());
            dto.setClinicalLastUpdated(latestAnyRecord.get().getCreatedAt().toLocalDate());
        } else {
            // Fallback: Default to ACTIVE only if the patient has zero records
            dto.setStatus("ACTIVE");
            dto.setClinicalLastUpdated(LocalDate.now());
        }

        // -----------------------------------------------------------
        // FIX PART B: GET PRIMARY DIAGNOSIS (From Latest Diagnosis)
        // -----------------------------------------------------------
        // We explicitly hunt for the disease name, ignoring Audit logs
        Optional<EhrRecord> latestDiagnosisOpt = ehrRecordRepository
                .findFirstByPatientIdAndEhrRecordTypeOrderByCreatedAtDesc(patient.getId(), EhrRecordType.DIAGNOSIS);

        if (latestDiagnosisOpt.isPresent()) {
            EhrRecord record = latestDiagnosisOpt.get();
            DiagnosisDetails details = parseJson(record.getDescription(), DiagnosisDetails.class);

            if (details != null) {
                dto.setPrimaryDiagnosis(details.getDiagnosisName());
            } else {
                dto.setPrimaryDiagnosis("Error Parsing Diagnosis");
            }

            // Note: We don't overwrite setClinicalLastUpdated here,
            // because the Status update (Part A) is usually more recent.

        } else {
            dto.setPrimaryDiagnosis("No Diagnosis");
        }

        return dto;
    }



    public void updateStatus(UpdateStatusDto request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + request.getDoctorId()));

        EhrRecord record = new EhrRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setEhrRecordType(EhrRecordType.AUDIT);
        record.setPatientStatus(request.getPatientStatus());

        try {
            AuditDetails details = new AuditDetails();
            details.setAction("STATUS_UPDATE");
            details.setPerformedBy(doctor.getFullName());
            details.setEntity("PATIENT");
            details.setSource("EHR_DASHBOARD");
            details.setDetails("Patient status changed to " + request.getPatientStatus());
            record.setDescription(objectMapper.writeValueAsString(details));
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON for status update", e);
        }

        ehrRecordRepository.save(record);
    }

    public PatientEhrDataDto getPatientEhr(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        List<EhrRecord> allRecords = ehrRecordRepository.findByPatientIdOrderByCreatedAtDesc(patientId);

        PatientEhrDataDto dto = new PatientEhrDataDto();
        dto.setPatientId(patient.getId());
        dto.setFullName(patient.getFullName());
        dto.setGender(patient.getGender().name());
        dto.setDob(patient.getDob());

        // Map Diagnoses
        List<DiagnosisDetails> diagnosisList = allRecords.stream()
                .filter(r -> r.getEhrRecordType() == EhrRecordType.DIAGNOSIS)
                .map(r -> parseJson(r.getDescription(), DiagnosisDetails.class))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
        dto.setDiagnoses(diagnosisList);

        // Map Lab Results
        List<LabResultDetails> labList = allRecords.stream()
                .filter(r -> r.getEhrRecordType() == EhrRecordType.LAB_RESULT)
                .map(r -> parseJson(r.getDescription(), LabResultDetails.class))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
        dto.setLabResults(labList);
        // Map Audit History
        List<ActivityLogDto> auditList = allRecords.stream()
                .filter(r -> r.getEhrRecordType() == EhrRecordType.AUDIT)
                .map(r -> {
                    AuditDetails details = parseJson(r.getDescription(), AuditDetails.class);
                    if (details == null) return null;

                    ActivityLogDto log = new ActivityLogDto();
                    log.setUser(details.getPerformedBy());
                    log.setAction(details.getAction());
                    log.setLogEntry(details.getDetails());
                    log.setTime(r.getCreatedAt());
                    return log;
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
        dto.setAuditHistory(auditList);

        return dto;
    }

    public void updateRecord(EhrUpdateDto request) {
        EhrRecord record;
        boolean isUpdate = (request.getRecordId() != null);

        // ==================================================================================
        // STEP A: SETUP THE RECORD
        // ==================================================================================
        if (isUpdate) {
            // --- CASE 1: UPDATE EXISTING ---
            record = ehrRecordRepository.findById(request.getRecordId())
                    .orElseThrow(() -> new RuntimeException("Record not found: " + request.getRecordId()));

            if (!record.getPatient().getId().equals(request.getPatientId())) {
                throw new RuntimeException("Mismatch: Record does not belong to this patient");
            }
            // NOTE: For updates, we DO NOT touch the status.
            // We let the record keep whatever status it already has in the database.

        } else {
            // --- CASE 2: CREATE NEW ---
            record = new EhrRecord();

            Patient patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            record.setPatient(patient);
            record.setCreatedAt(LocalDateTime.now());

            // --- SAFE INHERITANCE LOGIC ---
            // We look for the MOST RECENT record to copy the status from.
            Optional<EhrRecord> previousRecord = ehrRecordRepository.findFirstByPatientIdOrderByCreatedAtDesc(request.getPatientId());

            if (previousRecord.isPresent() && previousRecord.get().getPatientStatus() != null) {
                // Found a valid status, copy it.
                record.setPatientStatus(previousRecord.get().getPatientStatus());
            } else {
                // If no record exists OR the previous record had NULL status, default to ACTIVE.
                // This prevents "NULL" from spreading.
                record.setPatientStatus(com.Team2.Mediconnect.m3.Models.PatientStatus.ACTIVE);
            }
        }

        // ==================================================================================
        // STEP B: COMMON FIELDS
        // ==================================================================================
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        record.setDoctor(doctor);
        record.setEhrRecordType(request.getRecordType());

        // Save Data & Audit
        try {
            record.setDescription(objectMapper.writeValueAsString(request.getData()));
            ehrRecordRepository.save(record);

            String action = isUpdate ? "UPDATE" : "CREATE";
            createSmartAuditLog(record, request.getData(), doctor, action);

        } catch (Exception e) {
            throw new RuntimeException("Error processing record data", e);
        }
    }

    // --- HELPER METHODS (NOW CORRECTLY INSIDE THE CLASS) ---

    private void createSmartAuditLog(EhrRecord sourceRecord, Map<String, Object> data, Doctor doctor, String actionType) {
        try {
            EhrRecord audit = new EhrRecord();
            audit.setPatient(sourceRecord.getPatient());
            audit.setDoctor(doctor);
            audit.setEhrRecordType(EhrRecordType.AUDIT);
            audit.setCreatedAt(LocalDateTime.now());

            AuditDetails details = new AuditDetails();
            details.setAction(actionType);
            details.setPerformedBy(doctor.getFullName());
            details.setSource("EHR_EDITOR");
            details.setEntity(sourceRecord.getEhrRecordType().name());

            String summary = "Record Modified";
            if (sourceRecord.getEhrRecordType() == EhrRecordType.DIAGNOSIS) {
                summary = actionType + " Dx: " + data.getOrDefault("diagnosisName", "Unknown");
            } else if (sourceRecord.getEhrRecordType() == EhrRecordType.LAB_RESULT) {
                summary = actionType + " Lab: " + data.getOrDefault("testName", "Unknown");
            }

            details.setDetails(summary);
            audit.setDescription(objectMapper.writeValueAsString(details));

            ehrRecordRepository.save(audit);

        } catch (Exception e) {
            System.err.println("Failed to save audit log: " + e.getMessage());
        }
    }

    private <T> T parseJson(String json, Class<T> clazz) {
        try {
            if (json == null || json.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            System.err.println("JSON Error: " + e.getMessage());
            return null;
        }
    }
}