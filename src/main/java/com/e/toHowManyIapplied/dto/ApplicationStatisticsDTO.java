package com.e.toHowManyIapplied.dto;

import java.util.Map;

import com.e.toHowManyIapplied.model.ApplicationStatus;

public class ApplicationStatisticsDTO {

    private long totalApplications;
    private Map<ApplicationStatus, Long> statusCounts;

    public ApplicationStatisticsDTO() {}

    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }

    public Map<ApplicationStatus, Long> getStatusCounts() { return statusCounts; }
    public void setStatusCounts(Map<ApplicationStatus, Long> statusCounts) { this.statusCounts = statusCounts; }
}
