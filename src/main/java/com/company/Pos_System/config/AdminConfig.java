package com.company.Pos_System.config;

import com.company.Pos_System.models.enums.UserRole;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class AdminConfig {

    @Value("${app.admin-username}")
    private String adminUsername;

    @Value("${app.user-password}")
    private String userPassword;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    /*@Bean
    CommandLineRunner initAdmin(UserRepository userRepository) {
        return args -> {
            List<Users> admins = userRepository.findByRoleAndDeletedAtIsNull(UserRole.ADMIN);
            if (admins.isEmpty()) {
                Users admin = Users.builder()
                        .fullName("Bobur Toshniyozov")
                        .username(adminUsername)
                        .password(bCryptPasswordEncoder.encode(userPassword)) // Secure default password
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(admin);
                System.out.println("✅ Default ADMIN user created!");
            } else {
                System.out.println("❌ Admin user already exists!");
            }
        };
    }*/

}
