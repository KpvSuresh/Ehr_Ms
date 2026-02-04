package com.mediconnect.ehr1.Repository;

import com.Team2.Mediconnect.m2.Models.Doctor;
import com.mediconnect.ehr1.Models.EhrRecord;
import com.mediconnect.ehr1.Models.EhrRecordType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EhrRecordRepository extends JpaRepository<EhrRecord, Long>, JpaSpecificationExecutor<EhrRecord> {

    // 2. ADD THIS EXACT METHOD
    // (Spring automatically translates this to "ORDER BY created_at DESC LIMIT 1")
    Optional<EhrRecord> findFirstByPatientIdOrderByCreatedAtDesc(Long patientId);

    // 3. Keep this one for the Diagnosis name
    Optional<EhrRecord> findFirstByPatientIdAndEhrRecordTypeOrderByCreatedAtDesc(Long patientId, EhrRecordType type);

    // 4. Keep this for the full history
    List<EhrRecord> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 " +
            "THEN (COUNT(CASE WHEN e.outcome = 'RECOVERED' THEN 1 END) * 100.0 / COUNT(e)) " +
            "ELSE 0 END " +
            "FROM EhrRecord e")
    Double getRecoveryRate();

    @Query("SELECT new map(CAST(e.patientStatus AS string) as key, COUNT(e) as value) " +
            "FROM EhrRecord e " +
            "WHERE e.id = (" +
            "    SELECT MAX(sub.id) " +
            "    FROM EhrRecord sub " +
            "    WHERE sub.patient = e.patient" +
            ") " +
            "GROUP BY e.patientStatus")
    List<Map<String, Object>> countByPatientStatusGroup();

    // 3. LINE GRAPH (Part B): Monthly Recoveries
    // Groups by Month and counts only 'RECOVERED' outcomes
    @Query(value = "SELECT MONTHNAME(created_at) as month, COUNT(*) as count " +
            "FROM ehr_records " +
            "WHERE outcome = 'RECOVERED' " +
            "AND created_at >= DATE_SUB(NOW(), INTERVAL 12 MONTH) " +
            "GROUP BY MONTH(created_at), MONTHNAME(created_at) " +
            "ORDER BY MONTH(created_at)", nativeQuery = true)
    List<Map<String, Object>> getMonthlyRecoveries();


    // We use 'createdAt' which is LocalDateTime, so we need to handle range carefully
    @Query("SELECT COUNT(e) FROM EhrRecord e WHERE e.patientStatus = 'INPATIENT' AND e.createdAt BETWEEN :start AND :end")
    long countAdmittedByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(e) FROM EhrRecord e WHERE e.patientStatus = 'DISCHARGED' AND e.createdAt BETWEEN :start AND :end")
    long countDischargedByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(e) FROM EhrRecord e WHERE e.outcome = 'RECOVERED' AND e.createdAt BETWEEN :start AND :end")
    long countRecoveredByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(e) FROM EhrRecord e WHERE e.createdAt BETWEEN :start AND :end")
    long countTotalByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("SELECT e FROM EhrRecord e WHERE e.patient.id IN :patientIds " +
            "AND e.createdAt = (SELECT MAX(e2.createdAt) FROM EhrRecord e2 WHERE e2.patient.id = e.patient.id)")
    List<EhrRecord> findLatestRecordsByPatientIds(@Param("patientIds") List<Long> patientIds);

    static Specification<EhrRecord> getEhrSpec(String period, String department) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            LocalDate now = LocalDate.now();

            // 1. DATE RANGE (Same logic as Appointments)
            LocalDate startDate;
            String selectedPeriod = (period != null) ? period : "12m";
            switch (selectedPeriod) {
                case "1m" -> startDate = now.minusMonths(1);
                case "3m" -> startDate = now.minusMonths(3);
                case "6m" -> startDate = now.minusMonths(6);
                default -> startDate = now.minusMonths(12);
            }
            predicates.add(cb.between(root.get("createdAt"), startDate.atStartOfDay(), now.atStartOfDay()));

            // 2. DEPARTMENT FILTER
            if (department != null && !department.equalsIgnoreCase("all")) {
                Join<EhrRecord, Doctor> doctorJoin = root.join("doctor");
                predicates.add(cb.equal(doctorJoin.get("specialization"), department));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}