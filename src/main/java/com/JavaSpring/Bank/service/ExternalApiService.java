package main.java.com.JavaSpring.Bank.service;

import com.JavaSpring.Bank.dto.external.ExternalUserDTO;
import com.JavaSpring.Bank.dto.response.UserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * External API Service - Handles integration with JSONPlaceholder API
 * This fulfills the "nested API calling" requirement
 */
@Service
public class ExternalApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);
    
    private final WebClient webClient;
    
    @Value("${external.api.jsonplaceholder.base-url:https://jsonplaceholder.typicode.com}")
    private String baseUrl;
    
    @Value("${external.api.jsonplaceholder.timeout:5000}")
    private int timeout;
    
    public ExternalApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    /**
     * Fetch all users from JSONPlaceholder API
     * Demonstrates external API integration
     */
    public List<ExternalUserDTO> fetchExternalUsers() {
        logger.info("Fetching users from external API: {}", baseUrl);
        
        try {
            List<ExternalUserDTO> users = webClient.get()
                    .uri(baseUrl + "/users")
                    .retrieve()
                    .bodyToFlux(ExternalUserDTO.class)
                    .collectList()
                    .timeout(Duration.ofMillis(timeout))
                    .block();
            
            logger.info("Successfully fetched {} users from external API", users != null ? users.size() : 0);
            return users;
            
        } catch (Exception e) {
            logger.error("Error fetching users from external API: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch users from external API", e);
        }
    }
    
    /**
     * Fetch single user from JSONPlaceholder API
     * @param externalId - external user ID
     */
    public ExternalUserDTO fetchExternalUser(Long externalId) {
        logger.info("Fetching user {} from external API: {}", externalId, baseUrl);
        
        try {
            ExternalUserDTO user = webClient.get()
                    .uri(baseUrl + "/users/" + externalId)
                    .retrieve()
                    .bodyToMono(ExternalUserDTO.class)
                    .timeout(Duration.ofMillis(timeout))
                    .block();
            
            logger.info("Successfully fetched user {} from external API", externalId);
            return user;
            
        } catch (Exception e) {
            logger.error("Error fetching user {} from external API: {}", externalId, e.getMessage());
            throw new RuntimeException("Failed to fetch user from external API", e);
        }
    }
    
    /**
     * Import user from external API and save locally
     * This demonstrates the complete nested API flow:
     * Client -> Your API -> External API -> Your Database
     */
    @Transactional
    public UserResponseDTO importExternalUser(Long externalId, UserService userService) {
        logger.info("Importing user {} from external API", externalId);
        
        // Step 1: Fetch from external API
        ExternalUserDTO externalUser = fetchExternalUser(externalId);
        
        // Step 2: Transform to local format
        String[] nameParts = externalUser.getName().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        com.JavaSpring.Bank.dto.request.UserRequestDTO userRequest = 
            new com.JavaSpring.Bank.dto.request.UserRequestDTO(
                firstName,
                lastName,
                externalUser.getEmail(),
                externalUser.getPhone(),
                externalUser.getAddress() != null ? externalUser.getAddress().getCity() : ""
            );
        
        // Step 3: Save to local database
        UserResponseDTO savedUser = userService.createUser(userRequest);
        
        logger.info("Successfully imported user {} as local user {}", externalId, savedUser.getId());
        return savedUser;
    }
}