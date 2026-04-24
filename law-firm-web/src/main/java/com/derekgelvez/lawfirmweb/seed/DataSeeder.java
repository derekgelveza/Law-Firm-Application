package com.derekgelvez.lawfirmweb.seed;

import com.derekgelvez.lawfirmauth.model.Role;
import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {


        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            Users superAdmin = new Users();
            superAdmin.setFirstName("Super");
            superAdmin.setLastName("Admin");
            superAdmin.setEmail(adminEmail);
            superAdmin.setPassword(passwordEncoder.encode(adminPassword));
            superAdmin.setRole(Role.SUPER_ADMIN);

            userRepository.save(superAdmin);

            System.out.println("✓ SUPER_ADMIN account created: " + adminEmail);
        } else {
            System.out.println("✓ SUPER_ADMIN already exists, skipping seed");
        }
    }
}