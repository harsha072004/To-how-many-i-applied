package com.e.toHowManyIapplied.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.e.toHowManyIapplied.dto.JobApplicationRequestDTO;
import com.e.toHowManyIapplied.dto.JobApplicationResponseDTO;
import com.e.toHowManyIapplied.model.ApplicationStatus;
import com.e.toHowManyIapplied.service.JobApplicationService;

import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(JobApplicationController.class)
class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JobApplicationService service;

    @Test
    void testCreateApplication_SuccessReturns201() throws Exception {
        // Arrange
        JobApplicationRequestDTO request = new JobApplicationRequestDTO();
        request.setCompanyName("Google");
        request.setRole("Java Dev");
        request.setAppliedDate(LocalDate.now());
        request.setStatus(ApplicationStatus.APPLIED);

        JobApplicationResponseDTO response = new JobApplicationResponseDTO();
        response.setId("abc-123");
        response.setCompanyName("Google");

        when(service.createApplication(any(JobApplicationRequestDTO.class))).thenReturn(response);

        // Act & Assert: Fire POST request and check status and JSON body
        mockMvc.perform(post("/api/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc-123"))
                .andExpect(jsonPath("$.companyName").value("Google"));
    }

    @Test
    void testCreateApplication_ValidationFailsReturns400() throws Exception {
        // Arrange: Create an invalid request (blank company name)
        JobApplicationRequestDTO invalidRequest = new JobApplicationRequestDTO();
        invalidRequest.setCompanyName(""); 
        invalidRequest.setRole("Java Dev");
        invalidRequest.setAppliedDate(LocalDate.now());
        invalidRequest.setStatus(ApplicationStatus.APPLIED);

        // Act & Assert: Expect a 400 Bad Request
        mockMvc.perform(post("/api/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
