package com.e.toHowManyIapplied.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.e.toHowManyIapplied.dto.JobApplicationRequestDTO;
import com.e.toHowManyIapplied.dto.JobApplicationResponseDTO;
import com.e.toHowManyIapplied.service.JobApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*") // Allows our future frontend to talk to this API
public class JobApplicationController {

    private final JobApplicationService service;

    // Constructor Injection
    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    // 1. Create a new job application
    @PostMapping
    public ResponseEntity<JobApplicationResponseDTO> createApplication(
            @Valid @RequestBody JobApplicationRequestDTO requestDTO) {
        JobApplicationResponseDTO createdApplication = service.createApplication(requestDTO);
        return new ResponseEntity<>(createdApplication, HttpStatus.CREATED); // Returns 201 Created
    }

    // 2. Get all applications
    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDTO>> getAllApplications() {
        List<JobApplicationResponseDTO> applications = service.getAllApplications();
        return ResponseEntity.ok(applications); // Returns 200 OK
    }

    // 3. Get application by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponseDTO> getApplicationById(@PathVariable String id) {
        JobApplicationResponseDTO application = service.getApplicationById(id);
        return ResponseEntity.ok(application); // Returns 200 OK
    }
}
