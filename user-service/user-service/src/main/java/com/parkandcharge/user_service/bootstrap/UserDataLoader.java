package com.parkandcharge.user_service.bootstrap;

import com.parkandcharge.user_service.model.User;
import com.parkandcharge.user_service.model.UserRole;
import com.parkandcharge.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserDataLoader {

    @Bean
    public CommandLineRunner loadUserData(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User driver1 = new User(null, "Driver One", "driver1@gmail.com", encoder.encode("password123"), UserRole.DRIVER);
                User driver2 = new User(null, "Driver Two", "driver2@gmail.com", encoder.encode("password123"), UserRole.DRIVER);

                User owner1 = new User(null, "Owner One", "owner1@gmail.com", encoder.encode("password123"), UserRole.OWNER);
                User owner2 = new User(null, "Owner Two", "owner2@gmail.com", encoder.encode("password123"), UserRole.OWNER);

                userRepository.save(driver1);
                userRepository.save(driver2);
                userRepository.save(owner1);
                userRepository.save(owner2);

                System.out.println("Sample users inserted.");
            }
        };
    }
}
