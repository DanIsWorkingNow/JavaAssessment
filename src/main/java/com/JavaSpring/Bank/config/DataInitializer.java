package com.JavaSpring.Bank.config;

import com.JavaSpring.Bank.entity.User;
import com.JavaSpring.Bank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Initialize the database with sample data
 * This ensures the H2 database has users to display
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            logger.info("Initializing database with sample users...");
            
            List<User> sampleUsers = Arrays.asList(
                new User("John", "Doe", "john.doe@example.com", "+1-555-0001", "New York"),
                new User("Jane", "Smith", "jane.smith@example.com", "+1-555-0002", "Los Angeles"),
                new User("Michael", "Johnson", "michael.johnson@example.com", "+1-555-0003", "Chicago"),
                new User("Emily", "Davis", "emily.davis@example.com", "+1-555-0004", "Houston"),
                new User("David", "Wilson", "david.wilson@example.com", "+1-555-0005", "Phoenix"),
                new User("Sarah", "Miller", "sarah.miller@example.com", "+1-555-0006", "Philadelphia"),
                new User("James", "Brown", "james.brown@example.com", "+1-555-0007", "San Antonio"),
                new User("Jessica", "Garcia", "jessica.garcia@example.com", "+1-555-0008", "San Diego"),
                new User("Robert", "Rodriguez", "robert.rodriguez@example.com", "+1-555-0009", "Dallas"),
                new User("Lisa", "Martinez", "lisa.martinez@example.com", "+1-555-0010", "San Jose"),
                new User("William", "Anderson", "william.anderson@example.com", "+1-555-0011", "Austin"),
                new User("Ashley", "Taylor", "ashley.taylor@example.com", "+1-555-0012", "Jacksonville"),
                new User("Christopher", "Thomas", "christopher.thomas@example.com", "+1-555-0013", "Fort Worth"),
                new User("Amanda", "Hernandez", "amanda.hernandez@example.com", "+1-555-0014", "Columbus"),
                new User("Matthew", "Moore", "matthew.moore@example.com", "+1-555-0015", "Charlotte"),
                new User("Jennifer", "Martin", "jennifer.martin@example.com", "+1-555-0016", "San Francisco"),
                new User("Joshua", "Jackson", "joshua.jackson@example.com", "+1-555-0017", "Indianapolis"),
                new User("Stephanie", "Thompson", "stephanie.thompson@example.com", "+1-555-0018", "Seattle"),
                new User("Andrew", "White", "andrew.white@example.com", "+1-555-0019", "Denver"),
                new User("Michelle", "Lopez", "michelle.lopez@example.com", "+1-555-0020", "Washington"),
                new User("Kevin", "Lee", "kevin.lee@example.com", "+1-555-0021", "Boston"),
                new User("Laura", "Gonzalez", "laura.gonzalez@example.com", "+1-555-0022", "El Paso"),
                new User("Brian", "Harris", "brian.harris@example.com", "+1-555-0023", "Detroit"),
                new User("Nicole", "Clark", "nicole.clark@example.com", "+1-555-0024", "Nashville"),
                new User("Daniel", "Lewis", "daniel.lewis@example.com", "+1-555-0025", "Portland"),
                new User("Melissa", "Robinson", "melissa.robinson@example.com", "+1-555-0026", "Oklahoma City"),
                new User("Anthony", "Walker", "anthony.walker@example.com", "+1-555-0027", "Las Vegas"),
                new User("Rebecca", "Perez", "rebecca.perez@example.com", "+1-555-0028", "Louisville"),
                new User("Mark", "Hall", "mark.hall@example.com", "+1-555-0029", "Baltimore"),
                new User("Kimberly", "Young", "kimberly.young@example.com", "+1-555-0030", "Milwaukee")
            );
            
            userRepository.saveAll(sampleUsers);
            logger.info("Successfully initialized database with {} sample users", sampleUsers.size());
        } else {
            logger.info("Database already contains {} users, skipping initialization", userRepository.count());
        }
    }
}