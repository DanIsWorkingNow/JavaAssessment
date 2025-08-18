package test.java.com.JavaSpring.Bank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * UserControllerTest - Complete test implementation for REST API
 * Tests all endpoints with proper assertions
 */
@SpringBootTest
@AutoConfigureTestMvc
@TestPropertySource(properties = "spring.profiles.active=test")
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.users", hasSize(10)))
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.pageSize", is(10)))
                .andExpect(jsonPath("$.totalPages", greaterThan(0)));
    }
    
    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()));
    }
    
    @Test
    void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("User not found")));
    }
    
    @Test
    void testCreateUser() throws Exception {
        String userJson = """
            {
                "firstName": "Test",
                "lastName": "User",
                "email": "test@example.com",
                "phone": "+1-555-TEST",
                "city": "Test City"
            }
            """;
        
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("User")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }
    
    @Test
    void testCreateUserValidationFailed() throws Exception {
        String invalidUserJson = """
            {
                "firstName": "",
                "email": "invalid-email"
            }
            """;
        
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")));
    }
    
    @Test
    void testUpdateUser() throws Exception {
        String userJson = """
            {
                "firstName": "Updated",
                "lastName": "User",
                "email": "updated@example.com",
                "phone": "+1-555-UPDT",
                "city": "Updated City"
            }
            """;
        
        mockMvc.perform(put("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }
    
    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User deleted successfully")))
                .andExpect(jsonPath("$.status", is("success")));
    }
    
    @Test
    void testGetExternalUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users/external"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].source", is("JSONPlaceholder API")));
    }
    
    @Test
    void testImportUserFromExternalApi() throws Exception {
        mockMvc.perform(post("/api/v1/users/import/1"))
                .andExpected(status().isCreated())
                .andExpect(jsonPath("$.source", is("JSONPlaceholder API")))
                .andExpect(jsonPath("$.externalId", is(1)));
    }
    
    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/v1/users/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("User Management API")));
    }
    
    @Test
    void testPaginationWithFiltering() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("size", "10")
                .param("city", "New York")
                .param("keyword", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageSize", is(10)));
    }
}