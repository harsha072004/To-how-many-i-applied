# To-how-many-i-applied
To How Many I Applied — A Spring Boot-based job application tracker that allows users to record, search, update, and manage job applications using REST APIs and JSON file-based storage without a database.

# 💼 To How Many I Applied - Job Application Tracker

A full-stack web application built to track job applications, monitor follow-up dates, and analyze application statistics. 

Unlike standard CRUD applications that rely on Spring Data JPA and a database, this project implements a **custom File-based Repository using Java NIO and Jackson**. This demonstrates a deeper understanding of core Java I/O, JSON serialization, concurrency, and thread safety.

## 🚀 Features
* **Full CRUD Operations:** Create, Read, Update, and Delete job applications.
* **Custom File Persistence:** Data is stored in a structured JSON file (`data/job-applications.json`).
* **Thread-Safe Repository:** Uses Java `synchronized` blocks to prevent race conditions during concurrent file read/writes.
* **Dynamic Search & Filtering:** Filter applications by company, role, or status using Java 8 Streams.
* **Derived Data Calculations:** Automatically calculates "Days Since Applied" and suggests follow-up dates using Java `ChronoUnit`.
* **Global Exception Handling:** Clean, standardized REST API error responses using `@RestControllerAdvice`.
* **Responsive Frontend Dashboard:** Built with vanilla HTML/CSS/JS and the Fetch API (served directly from Spring Boot's static folder).

## 🛠️ Tech Stack
* **Backend:** Java 21, Spring Boot 3, Spring Web, Spring Validation
* **JSON Processing:** Jackson (`ObjectMapper`)
* **Frontend:** HTML5, CSS3, Vanilla JavaScript (Fetch API)
* **Build Tool:** Maven

## 🏗️ Architecture Layering
The backend strictly follows a layered Clean Architecture:
1. `Controller`: Handles HTTP mapping and Request/Response DTOs.
2. `Service`: Contains all business logic (e.g., calculating follow-ups, determining if a company responded).
3. `Repository`: Handles Java File I/O and JSON conversion.
4. `Model`: The core domain entities.
5. `Exception`: Centralized error handling.

## ⚙️ How to Run Locally

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/job-application-tracker.git](https://github.com/yourusername/job-application-tracker.git)
   cd job-application-tracker
