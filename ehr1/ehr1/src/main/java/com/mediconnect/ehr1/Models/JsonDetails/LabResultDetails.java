package com.mediconnect.ehr1.Models.JsonDetails;

import java.time.LocalDate;

public class LabResultDetails {
    private String testName;
    private String result;
    private String referenceRange;
    private String status;
    private LocalDate testDate;

    // --- 1. CONSTRUCTORS ---

    // Default Constructor (Required for JSON parsing)
    public LabResultDetails() {
    }

    // All-Args Constructor
    public LabResultDetails(String testName, String result, String referenceRange, String status, LocalDate testDate) {
        this.testName = testName;
        this.result = result;
        this.referenceRange = referenceRange;
        this.status = status;
        this.testDate = testDate;
    }

    // --- 2. GETTERS AND SETTERS ---

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReferenceRange() {
        return referenceRange;
    }

    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }
}