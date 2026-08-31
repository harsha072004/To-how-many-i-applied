package com.e.toHowManyIapplied.dto;

import com.e.toHowManyIapplied.model.ApplicationStatus;

import jakarta.validation.constraints.NotNull;

public class StatusUpdateDTO {

    @NotNull(message = "Status cannot be null")
    private ApplicationStatus status;

    public StatusUpdateDTO() {}

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
}
