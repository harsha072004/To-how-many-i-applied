package com.e.toHowManyIapplied.repository;




import jakarta.annotation.PostConstruct;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Repository;

import com.e.toHowManyIapplied.model.JobApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JobApplicationRepository {

    private final ObjectMapper objectMapper;
    private final Path filePath = Paths.get("data", "job-applications.json");

    // Constructor Injection (Spring Boot automatically provides the ObjectMapper)
    public JobApplicationRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Runs automatically right after Spring creates this bean
    @PostConstruct
    public void init() {
        try {
            // Create the 'data' directory if it doesn't exist
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            // Create the JSON file with an empty array if it doesn't exist
            if (!Files.exists(filePath)) {
                Files.writeString(filePath, "[]");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize data file", e);
        }
    }

    // --- Helper Methods for File I/O ---

    // Reads the JSON file and converts it into a List of JobApplication objects
    private synchronized List<JobApplication> readFromFile() {
        try {
            return objectMapper.readValue(filePath.toFile(), new TypeReference<List<JobApplication>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to read applications from file", e);
        }
    }

    // Converts the List of JobApplication objects back to JSON and overwrites the file
    private synchronized void writeToFile(List<JobApplication> applications) {
        try {
            // writeValue() automatically handles the conversion and file writing
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), applications);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write applications to file", e);
        }
    }

    // --- Core Repository Methods ---

    public List<JobApplication> findAll() {
        return readFromFile();
    }

    public Optional<JobApplication> findById(String id) {
        return findAll().stream()
                .filter(app -> app.getId().equals(id))
                .findFirst();
    }

    public JobApplication save(JobApplication application) {
        List<JobApplication> applications = readFromFile();
        
        // Check if application already exists (for updates)
        boolean exists = false;
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getId().equals(application.getId())) {
                applications.set(i, application); // Update existing
                exists = true;
                break;
            }
        }
        
        // If it doesn't exist, it's a new record
        if (!exists) {
            applications.add(application);
        }
        
        writeToFile(applications);
        return application;
    }

    public boolean deleteById(String id) {
        List<JobApplication> applications = readFromFile();
        boolean removed = applications.removeIf(app -> app.getId().equals(id));
        if (removed) {
            writeToFile(applications);
        }
        return removed;
    }

    public void deleteAll() {
        writeToFile(new ArrayList<>());
    }
}
