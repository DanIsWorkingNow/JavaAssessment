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
        logger.info("Initializing database with Malaysian sample users...");
        
        List<User> sampleUsers = Arrays.asList(
            new User("Ahmad", "bin Abdullah", "ahmad.abdullah@company.com", "+60-12-345-6789", "Kuala Lumpur"),
            new User("Siti", "binti Aminah", "siti.aminah@company.com", "+60-19-876-5432", "Johor Bahru"),
            new User("Lim", "Wei Ming", "lim.weiming@company.com", "+60-16-234-5678", "Penang"),
            new User("Priya", "Devi", "priya.devi@company.com", "+60-13-987-6543", "Ipoh"),
            new User("Muhammad", "Farid", "muhammad.farid@company.com", "+60-17-456-7890", "Shah Alam"),
            new User("Tan", "Ai Ling", "tan.ailing@company.com", "+60-12-789-0123", "Malacca"),
            new User("Raj", "Kumar", "raj.kumar@company.com", "+60-19-321-6547", "Kuching"),
            new User("Fatimah", "binti Hassan", "fatimah.hassan@company.com", "+60-16-654-3210", "Kota Kinabalu"),
            new User("Wong", "Chee Keong", "wong.cheekeong@company.com", "+60-13-567-8901", "Seremban"),
            new User("Nurul", "Aina", "nurul.aina@company.com", "+60-17-890-1234", "Petaling Jaya"),
            new User("Ravi", "Shankar", "ravi.shankar@company.com", "+60-12-123-4567", "Klang"),
            new User("Lee", "Mei Yee", "lee.meiyee@company.com", "+60-19-765-4321", "Alor Setar"),
            new User("Azman", "bin Omar", "azman.omar@company.com", "+60-16-345-6789", "Kuantan"),
            new User("Kavitha", "Balan", "kavitha.balan@company.com", "+60-13-876-5432", "Taiping"),
            new User("Ong", "Boon Huat", "ong.boonhuat@company.com", "+60-17-234-5678", "Miri"),
            new User("Zainab", "binti Yusof", "zainab.yusof@company.com", "+60-12-987-6543", "Sandakan"),
            new User("Kumar", "Selvam", "kumar.selvam@company.com", "+60-19-456-7890", "Subang Jaya"),
            new User("Chong", "Li Hua", "chong.lihua@company.com", "+60-16-789-0123", "Sibu"),
            new User("Salmah", "binti Rahmat", "salmah.rahmat@company.com", "+60-13-321-6547", "Putrajaya"),
            new User("Devi", "Krishnan", "devi.krishnan@company.com", "+60-17-654-3210", "Cyberjaya"),
            new User("Cheah", "Wee Loon", "cheah.weeloon@company.com", "+60-12-567-8901", "Batu Pahat"),
            new User("Halim", "bin Rashid", "halim.rashid@company.com", "+60-19-890-1234", "Tawau"),
            new User("Meera", "Patel", "meera.patel@company.com", "+60-16-123-4567", "Ampang"),
            new User("Yap", "Sook Cheng", "yap.sookcheng@company.com", "+60-13-765-4321", "Kajang"),
            new User("Faizal", "bin Zulkifli", "faizal.zulkifli@company.com", "+60-17-345-6789", "Nilai"),
            new User("Deepa", "Menon", "deepa.menon@company.com", "+60-12-876-5432", "Rawang"),
            new User("Lau", "Kah Seng", "lau.kahseng@company.com", "+60-19-234-5678", "Bentong"),
            new User("Rosmah", "binti Ali", "rosmah.ali@company.com", "+60-16-987-6543", "Temerloh"),
            new User("Anand", "Krishnan", "anand.krishnan@company.com", "+60-13-456-7890", "Sepang"),
            new User("Goh", "Swee Hock", "goh.sweehock@company.com", "+60-17-789-0123", "Puchong")
        );
        
        userRepository.saveAll(sampleUsers);
        logger.info("Successfully initialized database with {} Malaysian sample users", sampleUsers.size());
    } else {
        logger.info("Database already contains {} users, skipping initialization", userRepository.count());
    }
}
}