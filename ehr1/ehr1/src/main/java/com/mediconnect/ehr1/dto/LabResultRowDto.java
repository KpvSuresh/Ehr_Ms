package com.mediconnect.ehr1.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LabResultRowDto {
    private String testName;       // "HbA1c"
    private String labName;        // "CENTRAL LAB"
    private String result;         // "4.6 %"
    private String referenceRange; // "4.0 - 5.6"
    private LocalDate date;        // "2025-12-13"
    private String status;         // "NORMAL"
}