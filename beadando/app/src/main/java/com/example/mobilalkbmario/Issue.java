package com.example.mobilalkbmario;

import java.io.Serializable;

public class Issue implements Serializable {
    private String id; // Add ID field
    private String name;
    private int severity;
    private String description;
    private String reportedBy;

    public Issue() {
        // Required empty public constructor for Firestore
    }

    public Issue(String id, String name, int severity, String description, String reportedBy) {
        this.id = id;
        this.name = name;
        this.severity = severity;
        this.description = description;
        this.reportedBy = reportedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }
}
