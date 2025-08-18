package main.java.com.JavaSpring.Bank.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;
    
    @Size(max = 100, message = "City name cannot exceed 100 characters")
    private String city;
    
    // Constructors, getters, and setters
    // ... (generate all constructors, getters and setters)
}