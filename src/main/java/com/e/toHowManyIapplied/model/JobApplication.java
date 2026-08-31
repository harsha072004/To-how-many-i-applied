package com.e.toHowManyIapplied.model;

import java.time.LocalDate;
import java.util.Objects;

public class JobApplication {
	private String id;
    private String companyName;
    private String role;
    private LocalDate appliedDate;
    private ApplicationStatus status;
    private boolean heardBack;
    private String notes;
	@Override
	public int hashCode() {
		return Objects.hash(appliedDate, companyName, heardBack, id, notes, role, status);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobApplication other = (JobApplication) obj;
		return Objects.equals(appliedDate, other.appliedDate) && Objects.equals(companyName, other.companyName)
				&& heardBack == other.heardBack && Objects.equals(id, other.id) && Objects.equals(notes, other.notes)
				&& Objects.equals(role, other.role) && status == other.status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public LocalDate getAppliedDate() {
		return appliedDate;
	}
	public void setAppliedDate(LocalDate appliedDate) {
		this.appliedDate = appliedDate;
	}
	public ApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	public boolean isHeardBack() {
		return heardBack;
	}
	public void setHeardBack(boolean heardBack) {
		this.heardBack = heardBack;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public JobApplication(String id, String companyName, String role, LocalDate appliedDate, ApplicationStatus status,
			boolean heardBack, String notes) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.role = role;
		this.appliedDate = appliedDate;
		this.status = status;
		this.heardBack = heardBack;
		this.notes = notes;
	}
    
    
    
    JobApplication(){
    	
    }
}
