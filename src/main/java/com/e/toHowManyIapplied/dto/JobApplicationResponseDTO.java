package com.e.toHowManyIapplied.dto;

import java.time.LocalDate;

import com.e.toHowManyIapplied.model.ApplicationStatus;

public class JobApplicationResponseDTO {

    private String id;
    private String companyName;
    private String role;
    private LocalDate appliedDate;
    private ApplicationStatus status;
    private boolean heardBack;
    private String notes;

    // Default Constructor
    public JobApplicationResponseDTO() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public boolean isHeardBack() { return heardBack; }
    public void setHeardBack(boolean heardBack) { this.heardBack = heardBack; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}