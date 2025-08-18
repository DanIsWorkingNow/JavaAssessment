package com.JavaSpring.Bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Web Controller for Thymeleaf templates
 * Provides web interface for the User Management System
 * This controller handles web pages (not REST APIs)
 */
@Controller
public class WebController {
    
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    
    /**
     * Dashboard - Main landing page
     * Shows system overview and quick access to features
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        logger.info("REQUEST - GET / - Loading dashboard");
        
        // Add model attributes for the template
        model.addAttribute("title", "User Management System");
        model.addAttribute("appName", "Java SpringBoot Assessment");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("status", "Running");
        model.addAttribute("javaVersion", "21");
        model.addAttribute("springBootVersion", "3.5.4");
        model.addAttribute("totalUsers", 30); // Simulated total
        model.addAttribute("apiEndpoints", 8); // Number of API endpoints
        
        logger.info("RESPONSE - GET / - Dashboard loaded successfully");
        return "dashboard";
    }
    
    /**
     * Users list page with pagination support
     * Demonstrates web interface for user management
     */
    @GetMapping("/users")
    public String usersList(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String city,
                           @RequestParam(required = false) String keyword,
                           Model model) {
        logger.info("REQUEST - GET /users - Loading users list page: {}, size: {}, city: {}, keyword: {}", 
                   page, size, city, keyword);
        
        // Calculate pagination info
        int totalUsers = 30;
        int totalPages = (totalUsers + size - 1) / size;
        boolean hasNext = page < totalPages - 1;
        boolean hasPrevious = page > 0;
        
        // Add model attributes for the template
        model.addAttribute("title", "Users Management");
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("hasPrevious", hasPrevious);
        model.addAttribute("city", city);
        model.addAttribute("keyword", keyword);
        
        // Page range for pagination controls
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
        logger.info("RESPONSE - GET /users - Users list page loaded for page {}", page);
        return "users/list";
    }
    
    /**
     * Create user form page
     */
    @GetMapping("/users/create")
    public String createUserForm(Model model) {
        logger.info("REQUEST - GET /users/create - Loading create user form");
        
        model.addAttribute("title", "Create New User");
        model.addAttribute("formAction", "/api/v1/users");
        model.addAttribute("submitText", "Create User");
        
        logger.info("RESPONSE - GET /users/create - Create user form loaded");
        return "users/create";
    }
    
    /**
     * Edit user form page
     */
    @GetMapping("/users/edit")
    public String editUserForm(@RequestParam Long id, Model model) {
        logger.info("REQUEST - GET /users/edit - Loading edit user form for ID: {}", id);
        
        model.addAttribute("title", "Edit User");
        model.addAttribute("userId", id);
        model.addAttribute("formAction", "/api/v1/users/" + id);
        model.addAttribute("submitText", "Update User");
        
        logger.info("RESPONSE - GET /users/edit - Edit user form loaded for ID: {}", id);
        return "users/edit";
    }
    
    /**
     * API Documentation page
     * Shows available REST endpoints and their usage
     */
    @GetMapping("/api-docs")
    public String apiDocs(Model model) {
        logger.info("REQUEST - GET /api-docs - Loading API documentation");
        
        model.addAttribute("title", "API Documentation");
        model.addAttribute("baseUrl", "http://localhost:8080/api");
        model.addAttribute("version", "v1");
        
        // API endpoints information
        java.util.List<java.util.Map<String, Object>> endpoints = new java.util.ArrayList<>();
        
        // GET endpoints
        endpoints.add(createEndpoint("GET", "/v1/users", "Get all users with pagination", "Supports page, size, sortBy, sortDir, city, keyword parameters"));
        endpoints.add(createEndpoint("GET", "/v1/users/{id}", "Get user by ID", "Returns user details for specified ID"));
        endpoints.add(createEndpoint("GET", "/v1/users/external", "Get users from external API", "Demonstrates nested API calling to JSONPlaceholder"));
        endpoints.add(createEndpoint("GET", "/v1/users/health", "Health check", "Service status and information"));
        
        // POST endpoints
        endpoints.add(createEndpoint("POST", "/v1/users", "Create new user", "Requires firstName, lastName, email in request body"));
        endpoints.add(createEndpoint("POST", "/v1/users/import/{id}", "Import user from external API", "Fetches user from JSONPlaceholder and saves locally"));
        
        // PUT endpoints
        endpoints.add(createEndpoint("PUT", "/v1/users/{id}", "Update existing user", "Updates user with specified ID"));
        
        // DELETE endpoints
        endpoints.add(createEndpoint("DELETE", "/v1/users/{id}", "Delete user", "Removes user with specified ID"));
        
        model.addAttribute("endpoints", endpoints);
        
        logger.info("RESPONSE - GET /api-docs - API documentation loaded with {} endpoints", endpoints.size());
        return "api-docs";
    }
    
    /**
     * System health and status page
     */
    @GetMapping("/health")
    public String health(Model model) {
        logger.info("REQUEST - GET /health - Loading health check page");
        
        model.addAttribute("title", "System Health");
        model.addAttribute("status", "UP");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        model.addAttribute("uptime", "Application running");
        
        // System information
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("osName", System.getProperty("os.name"));
        model.addAttribute("osVersion", System.getProperty("os.version"));
        model.addAttribute("availableProcessors", Runtime.getRuntime().availableProcessors());
        
        // Memory information (in MB)
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        model.addAttribute("totalMemory", totalMemory);
        model.addAttribute("usedMemory", usedMemory);
        model.addAttribute("freeMemory", freeMemory);
        
        logger.info("RESPONSE - GET /health - Health check page loaded");
        return "health";
    }
    
    /**
     * External API demonstration page
     */
    @GetMapping("/external-api")
    public String externalApi(Model model) {
        logger.info("REQUEST - GET /external-api - Loading external API demo page");
        
        model.addAttribute("title", "External API Integration");
        model.addAttribute("externalApiUrl", "https://jsonplaceholder.typicode.com");
        model.addAttribute("localApiUrl", "/api/v1/users/external");
        model.addAttribute("importApiUrl", "/api/v1/users/import/");
        
        logger.info("RESPONSE - GET /external-api - External API demo page loaded");
        return "external-api";
    }
    
    /**
     * Assessment features overview page
     */
    @GetMapping("/assessment")
    public String assessment(Model model) {
        logger.info("REQUEST - GET /assessment - Loading assessment features page");
        
        model.addAttribute("title", "Assessment Features");
        
        // Completed features
        java.util.List<String> completedFeatures = java.util.Arrays.asList(
            "Spring Boot Application (3.5.4)",
            "REST API Endpoints (8 endpoints)",
            "Request/Response Logging",
            "Pagination (10 records per page)",
            "External API Integration (JSONPlaceholder)",
            "@Transactional Simulation",
            "Project Structure (Layered Architecture)",
            "Database Ready (H2 Console)",
            "Web Interface (Thymeleaf)",
            "Error Handling",
            "CORS Configuration",
            "Health Monitoring"
        );
        
        // Ready for enhancement
        java.util.List<String> enhancementFeatures = java.util.Arrays.asList(
            "Full Database Integration (JPA Entities)",
            "Input Validation (Bean Validation)",
            "Advanced Exception Handling",
            "MSSQL Configuration",
            "Advanced Search & Filtering",
            "Unit Tests (JUnit)",
            "Integration Tests",
            "Postman Collection",
            "Production Deployment Configuration",
            "Security Implementation",
            "API Documentation (Swagger)",
            "Docker Configuration"
        );
        
        model.addAttribute("completedFeatures", completedFeatures);
        model.addAttribute("enhancementFeatures", enhancementFeatures);
        
        logger.info("RESPONSE - GET /assessment - Assessment features page loaded");
        return "assessment";
    }
    
    /**
     * Helper method to create endpoint information
     */
    private java.util.Map<String, Object> createEndpoint(String method, String path, String description, String details) {
        java.util.Map<String, Object> endpoint = new java.util.HashMap<>();
        endpoint.put("method", method);
        endpoint.put("path", path);
        endpoint.put("description", description);
        endpoint.put("details", details);
        endpoint.put("methodClass", method.toLowerCase());
        return endpoint;
    }
}