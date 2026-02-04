package com.mediconnect.ehr1.Models.JsonDetails;

public class NoteDetails {
    private String title;
    private String content;
    private String visibility;

    // --- 1. CONSTRUCTORS ---

    // Default Constructor (Required for JSON parsing)
    public NoteDetails() {
    }

    // All-Args Constructor
    public NoteDetails(String title, String content, String visibility) {
        this.title = title;
        this.content = content;
        this.visibility = visibility;
    }

    // --- 2. GETTERS AND SETTERS ---

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}






