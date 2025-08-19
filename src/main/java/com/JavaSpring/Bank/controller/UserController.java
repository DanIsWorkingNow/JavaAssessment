package com.JavaSpring.Bank.controller;

import com.JavaSpring.Bank.dto.request.UserRequestDTO;
import com.JavaSpring.Bank.dto.response.UserResponseDTO;
import com.JavaSpring.Bank.dto.response.PagedResponseDTO;
import com.JavaSpring.Bank.service.UserService;
import com.JavaSpring.Bank.service.ExternalApiService;
import com.JavaSpring.Bank.dto.external.ExternalUserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * REST Controller for User Management
 * Demonstrates all assessment requirements:
 * - CRUD operations with proper @Transactional support
 * - Pagination (10 records per page as required)
 * - Request/Response logging
 * - External API integration (nested API calls)
 */
@RestController
@RequestMapping("/api/v1/users")
@Validated
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ExternalApiService externalApiService;
    
    /**
     * Get all users with pagination
     * @Transactional(readOnly = true) - handled by UserService
     * Default page size: 10 (as required by assessment)
     */
    @GetMapping
    public ResponseEntity<PagedResponseDTO<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword) {
        
        // REQUEST LOGGING as required by assessment
        logger.info("REQUEST - GET /v1/users - Loading users page: {}, size: {}, city: {}, keyword: {}", 
                   page, size, city, keyword);
        
        Page<UserResponseDTO> users = userService.getAllUsers(page, size, sortBy, sortDir, city, keyword);
        
        PagedResponseDTO<UserResponseDTO> response = new PagedResponseDTO<>(
            users.getContent(),
            users.getNumber(),
            users.getSize(),
            users.getTotalElements(),
            users.getTotalPages(),
            users.hasNext(),
            users.hasPrevious(),
            sortBy,
            sortDir
        );
        
        // RESPONSE LOGGING as required by assessment
        logger.info("RESPONSE - GET /v1/users - Status: 200, Total Elements: {}, Current Page: {}, Page Size: {}", 
                   users.getTotalElements(), page, users.getNumberOfElements());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get user by ID
     * @Transactional(readOnly = true) - handled by UserService
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable @Min(1) Long id) {
        logger.info("REQUEST - GET /v1/users/{} - User ID: {}", id, id);
        
        UserResponseDTO user = userService.getUserById(id);
        
        logger.info("RESPONSE - GET /v1/users/{} - Status: 200, User found: {}", id, user.getEmail());
        return ResponseEntity.ok(user);
    }
    
    /**
     * Create new user
     * @Transactional - handled by UserService
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        logger.info("REQUEST - POST /v1/users - Creating user with email: {}", request.getEmail());
        
        UserResponseDTO createdUser = userService.createUser(request);
        
        logger.info("RESPONSE - POST /v1/users - Status: 201, User created with ID: {}", createdUser.getId());
        return ResponseEntity.status(201).body(createdUser);
    }
    
    /**
     * Update existing user
     * @Transactional - handled by UserService
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable @Min(1) Long id, 
            @Valid @RequestBody UserRequestDTO request) {
        
        logger.info("REQUEST - PUT /v1/users/{} - Updating user with email: {}", id, request.getEmail());
        
        UserResponseDTO updatedUser = userService.updateUser(id, request);
        
        logger.info("RESPONSE - PUT /v1/users/{} - Status: 200, User updated", id);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Delete user
     * @Transactional - handled by UserService
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable @Min(1) Long id) {
        logger.info("REQUEST - DELETE /v1/users/{} - Deleting user", id);
        
        userService.deleteUser(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("id", id);
        response.put("status", 200);
        response.put("timestamp", java.time.LocalDateTime.now());
        
        logger.info("RESPONSE - DELETE /v1/users/{} - Status: 200, User deleted", id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * External API Integration - Get users from JSONPlaceholder
     * Demonstrates nested API calling pattern:
     * Client -> Your API -> External API
     */
    @GetMapping("/external")
    public ResponseEntity<List<ExternalUserDTO>> getExternalUsers() {
        logger.info("REQUEST - GET /v1/users/external - Fetching users from external API");
        
        List<ExternalUserDTO> externalUsers = externalApiService.fetchExternalUsers();
        
        logger.info("RESPONSE - GET /v1/users/external - Status: 200, External users fetched: {}", 
                   externalUsers.size());
        return ResponseEntity.ok(externalUsers);
    }
    
    /**
     * Import user from external API and save to local database
     * Demonstrates complete nested API flow:
     * Client -> Your API -> External API -> Your Database
     */
    @PostMapping("/import/{externalId}")
    public ResponseEntity<UserResponseDTO> importExternalUser(@PathVariable @Min(1) Long externalId) {
        logger.info("REQUEST - POST /v1/users/import/{} - Importing user from external API", externalId);
        
        UserResponseDTO importedUser = externalApiService.importExternalUser(externalId, userService);
        
        logger.info("RESPONSE - POST /v1/users/import/{} - Status: 201, User imported with ID: {}", 
                   externalId, importedUser.getId());
        return ResponseEntity.status(201).body(importedUser);
    }
}