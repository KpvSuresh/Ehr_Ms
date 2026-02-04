package com.mediconnect.ehr1.dto;

import java.time.LocalDateTime;

public class ActivityLogDto {
    private String user;      // e.g., "Dr. Patel"
    private String action;    // e.g., "VIEW", "UPDATE"
    private LocalDateTime time;
    private String logEntry;  // e.g., "System event", "Added E11.9"

    // --- 1. CONSTRUCTORS ---

    // Default Constructor (Required for JSON parsing)
    public ActivityLogDto() {
    }

    // All-Args Constructor
    public ActivityLogDto(String user, String action, LocalDateTime time, String logEntry) {
        this.user = user;
        this.action = action;
        this.time = time;
        this.logEntry = logEntry;
    }

    // --- 2. GETTERS AND SETTERS ---

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getLogEntry() {
        return logEntry;
    }

    public void setLogEntry(String logEntry) {
        this.logEntry = logEntry;
    }
}