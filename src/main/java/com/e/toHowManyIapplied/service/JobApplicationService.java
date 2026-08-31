package com.e.toHowManyIapplied.service;



import org.springframework.stereotype.Service;
import java.time.LocalDate; 
import com.e.toHowManyIapplied.dto.JobApplicationRequestDTO;
import com.e.toHowManyIapplied.dto.JobApplicationResponseDTO;
import com.e.toHowManyIapplied.exception.ApplicationNotFoundException;
import com.e.toHowManyIapplied.model.JobApplication;
import com.e.toHowManyIapplied.repository.JobApplicationRepository;
import com.e.toHowManyIapplied.model.ApplicationStatus;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    // Constructor Injection
    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    public JobApplicationResponseDTO createApplication(JobApplicationRequestDTO requestDTO) {
        JobApplication application = new JobApplication();
        
        // 1. Generate a unique ID
        application.setId(UUID.randomUUID().toString());
        
        // 2. Map fields from Request DTO to Domain Model
        application.setCompanyName(requestDTO.getCompanyName());
        application.setRole(requestDTO.getRole());
        application.setAppliedDate(requestDTO.getAppliedDate());
        application.setStatus(requestDTO.getStatus());
        application.setNotes(requestDTO.getNotes());
        
        // 3. Business Logic: Calculate if we have heard back based on status
        application.setHeardBack(calculateHeardBack(requestDTO.getStatus()));

        // 4. Save to repository
        JobApplication savedApplication = repository.save(application);

        // 5. Convert saved model back to Response DTO
        return mapToResponseDTO(savedApplication);
    }

    public List<JobApplicationResponseDTO> getAllApplications() {
        return repository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public JobApplicationResponseDTO getApplicationById(String id) {
        JobApplication application = repository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with ID: " + id));
        return mapToResponseDTO(application);
    }

    // --- Helper Methods ---

    // Logic to determine if the company has responded
    private boolean calculateHeardBack(ApplicationStatus status) {
        return status != ApplicationStatus.APPLIED && 
               status != ApplicationStatus.NO_RESPONSE && 
               status != ApplicationStatus.WITHDRAWN;
    }

    // Mapper method: Converts Domain Model to Response DTO
    private JobApplicationResponseDTO mapToResponseDTO(JobApplication application) {
        JobApplicationResponseDTO responseDTO = new JobApplicationResponseDTO();
        responseDTO.setId(application.getId());
        responseDTO.setCompanyName(application.getCompanyName());
        responseDTO.setRole(application.getRole());
        responseDTO.setAppliedDate(application.getAppliedDate());
        responseDTO.setStatus(application.getStatus());
        responseDTO.setHeardBack(application.isHeardBack());
        responseDTO.setNotes(application.getNotes());
        return responseDTO;
    }
  

    // ... inside JobApplicationService class:

    public List<JobApplicationResponseDTO> searchApplications(String company, String role, LocalDate date, ApplicationStatus status) {
        return repository.findAll().stream()
                // If company is provided, check if companyName contains it (case-insensitive)
                .filter(app -> company == null || app.getCompanyName().toLowerCase().contains(company.toLowerCase()))
                
                // If role is provided, check if role contains it (case-insensitive)
                .filter(app -> role == null || app.getRole().toLowerCase().contains(role.toLowerCase()))
                
                // If date is provided, check for exact match
                .filter(app -> date == null || app.getAppliedDate().equals(date))
                
                // If status is provided, check for exact match
                .filter(app -> status == null || app.getStatus() == status)
                
                // Convert the filtered models to DTOs
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
