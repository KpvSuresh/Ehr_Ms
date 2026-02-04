package com.mediconnect.ehr1.Models.JsonDetails;

public class AuditDetails {
    private String action;
    private String entity;
    private String details;
    private String performedBy;
    private String source;

    // --- 1. CONSTRUCTORS ---

    // Default Constructor (Required for JSON parsing)
    public AuditDetails() {
    }

    // All-Args Constructor
    public AuditDetails(String action, String entity, String details, String performedBy, String source) {
        this.action = action;
        this.entity = entity;
        this.details = details;
        this.performedBy = performedBy;
        this.source = source;
    }

    // --- 2. GETTERS AND SETTERS ---

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}