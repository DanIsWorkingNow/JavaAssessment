package com.JavaSpring.Bank.controller;

import com.JavaSpring.Bank.service.UserService;
import com.JavaSpring.Bank.dto.response.UserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Web Controller for serving HTML pages
 * NOW USES REAL DATABASE instead of hardcoded simulation
 */
@Controller
public class WebController {
    
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    
    @Autowired
    private UserService userService;  // NOW USES REAL DATABASE!
    
    /**
     * Home page
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        logger.info("REQUEST - GET / - Loading dashboard");
        
        // Get actual database statistics
        try {
            Page<UserResponseDTO> users = userService.getAllUsers(0, 1, "id", "asc", null, null);
            long totalUsers = users.getTotalElements();
            
            model.addAttribute("title", "Java SpringBoot Assessment");
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("databaseStatus", "Connected to H2 Database");
            
            logger.info("RESPONSE - GET / - Dashboard loaded successfully with {} users", totalUsers);
        } catch (Exception e) {
            logger.error("Error loading dashboard: {}", e.getMessage());
            model.addAttribute("totalUsers", 0);
            model.addAttribute("databaseStatus", "Database connection error");
        }
        
        return "dashboard";
    }
    
    /**
     * Users list page - NOW USES REAL DATABASE
     */
    @GetMapping("/users")
    public String usersList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword,
            Model model) {
        
        logger.info("REQUEST - GET /users - Loading users list page: {}, size: {}, city: {}, keyword: {}", 
                   page, size, city, keyword);
        
        try {
            // GET REAL DATABASE DATA instead of simulation
            Page<UserResponseDTO> usersPage = userService.getAllUsers(page, size, "id", "asc", city, keyword);
            
            model.addAttribute("title", "Users Management");
            model.addAttribute("users", usersPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalPages", usersPage.getTotalPages());
            model.addAttribute("totalUsers", usersPage.getTotalElements());
            model.addAttribute("hasNext", usersPage.hasNext());
            model.addAttribute("hasPrevious", usersPage.hasPrevious());
            model.addAttribute("city", city);
            model.addAttribute("keyword", keyword);
            
            // Page range for pagination controls
            int startPage = Math.max(0, page - 2);
            int endPage = Math.min(usersPage.getTotalPages() - 1, page + 2);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            
            logger.info("RESPONSE - GET /users - Users list page loaded for page {} with {} users", 
                       page, usersPage.getNumberOfElements());
                       
        } catch (Exception e) {
            logger.error("Error loading users page: {}", e.getMessage());
            model.addAttribute("users", java.util.Collections.emptyList());
            model.addAttribute("totalUsers", 0);
            model.addAttribute("errorMessage", "Error loading users from database");
        }
        
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
     */
    @GetMapping("/api-docs")
    public String apiDocs(Model model) {
        logger.info("REQUEST - GET /api-docs - Loading API documentation");
        
        model.addAttribute("title", "API Documentation");
        
        // Real API endpoints count
        int totalEndpoints = 8; // GET, POST, PUT, DELETE, GET/{id}, GET/external, POST/import/{id}, etc.
        model.addAttribute("totalEndpoints", totalEndpoints);
        
        logger.info("RESPONSE - GET /api-docs - API documentation loaded with {} endpoints", totalEndpoints);
        return "api-docs";
    }

    @GetMapping("/api-explorer")
public String apiExplorer(Model model) {
    model.addAttribute("title", "API Explorer");
    return "api-explorer"; // Uses the enhanced viewer
}

@GetMapping("/api-docs-interactive")
public String interactiveDocs(Model model) {
    model.addAttribute("title", "Interactive API Documentation");
    return "api-docs-interactive"; // Uses the Swagger-like interface
}
}