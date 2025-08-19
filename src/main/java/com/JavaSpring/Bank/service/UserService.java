package com.JavaSpring.Bank.service;

import com.JavaSpring.Bank.dto.request.UserRequestDTO;
import com.JavaSpring.Bank.dto.response.UserResponseDTO;
import com.JavaSpring.Bank.entity.User;
import com.JavaSpring.Bank.exception.DuplicateResourceException;
import com.JavaSpring.Bank.exception.ResourceNotFoundException;
import com.JavaSpring.Bank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional(readOnly = true)
public Page<UserResponseDTO> getAllUsers(int page, int size, String sortBy, 
                                       String sortDir, String city, String keyword) {
    logger.info("Fetching users with pagination - Page: {}, Size: {}, SortBy: {}, " +
               "SortDir: {}, City: {}, Keyword: {}", page, size, sortBy, sortDir, city, keyword);
    
    Sort sort = sortDir.equalsIgnoreCase("desc") ? 
               Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    
    Pageable pageable = PageRequest.of(page, size, sort);
    
    // CRITICAL FIX: Handle empty strings as null
    String normalizedCity = (city != null && !city.trim().isEmpty()) ? city.trim() : null;
    String normalizedKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
    
    Page<User> users;
    if (normalizedCity != null || normalizedKeyword != null) {
        logger.info("Using filtered query with city: {}, keyword: {}", normalizedCity, normalizedKeyword);
        users = userRepository.findByCityAndKeyword(normalizedCity, normalizedKeyword, pageable);
    } else {
        logger.info("Using findAll query for all users");
        users = userRepository.findAll(pageable);
    }
    
    logger.info("Query returned {} users out of {} total", users.getNumberOfElements(), users.getTotalElements());
    
    return users.map(UserResponseDTO::new);
}
    
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        return new UserResponseDTO(user);
    }
    
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {
        logger.info("Creating new user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }
        
        User user = new User(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getPhone(),
            request.getCity()
        );
        
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        
        return new UserResponseDTO(savedUser);
    }
    
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        logger.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        // Check for email uniqueness if email is being changed
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }
        
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setCity(request.getCity());
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return new UserResponseDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }
}