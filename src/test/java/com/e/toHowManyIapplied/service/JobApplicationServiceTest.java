package com.e.toHowManyIapplied.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.e.toHowManyIapplied.dto.JobApplicationRequestDTO;
import com.e.toHowManyIapplied.dto.JobApplicationResponseDTO;
import com.e.toHowManyIapplied.exception.ApplicationNotFoundException;
import com.e.toHowManyIapplied.model.ApplicationStatus;
import com.e.toHowManyIapplied.model.JobApplication;
import com.e.toHowManyIapplied.repository.JobApplicationRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JobApplicationServiceTest {

 @Mock
 private JobApplicationRepository repository;

 @InjectMocks
 private JobApplicationService service;

 private JobApplicationRequestDTO requestDTO;
 private JobApplication savedModel;

 @BeforeEach
 void setUp() {
     // Initialize Mockito manually (Bypasses Eclipse extension bugs)
     MockitoAnnotations.openMocks(this);

     // Set up dummy data before each test
     requestDTO = new JobApplicationRequestDTO();
     requestDTO.setCompanyName("Tech Corp");
     requestDTO.setRole("Backend Developer");
     requestDTO.setAppliedDate(LocalDate.now());
     requestDTO.setStatus(ApplicationStatus.INTERVIEW);

     savedModel = new JobApplication(
             "123-abc", "Tech Corp", "Backend Developer", 
             LocalDate.now(), ApplicationStatus.INTERVIEW, true, ""
     );
 }

 @Test
 void testCreateApplication_CalculatesHeardBackCorrectly() {
     // Arrange
     when(repository.save(any(JobApplication.class))).thenReturn(savedModel);

     // Act
     JobApplicationResponseDTO response = service.createApplication(requestDTO);

     // Assert
     assertNotNull(response.getId());
     assertTrue(response.isHeardBack()); 
     assertEquals("Tech Corp", response.getCompanyName());
     
     verify(repository, times(1)).save(any(JobApplication.class));
 }

 @Test
 void testGetApplicationById_ThrowsExceptionWhenNotFound() {
     // Arrange
     when(repository.findById("fake-id")).thenReturn(Optional.empty());

     // Act & Assert
     assertThrows(ApplicationNotFoundException.class, () -> {
         service.getApplicationById("fake-id");
     });
 }
}