package com.e.toHowManyIapplied.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

import com.e.toHowManyIapplied.model.ApplicationStatus;

public class JobApplicationRequestDTO {

    @NotBlank(message = "Company name is mandatory")
    private String companyName;

    @NotBlank(message = "Job role is mandatory")
    private String role;

    @NotNull(message = "Application date is mandatory")
    @PastOrPresent(message = "Application date cannot be in the future")
    private LocalDate appliedDate;

    @NotNull(message = "Application status is mandatory")
    private ApplicationStatus status;

    private String notes;

    // Default Constructor
    public JobApplicationRequestDTO() {}

    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}