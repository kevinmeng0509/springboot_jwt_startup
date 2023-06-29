package com.fitit100.village;

import com.fitit100.village.models.AppUser;
import com.fitit100.village.models.Role;
import com.fitit100.village.repository.RoleRepository;
import com.fitit100.village.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class VillageApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VillageApiApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
            Role adminRole = roleRepository.save(
                    new Role("ADMIN")
            );
            roleRepository.save(new Role("USER"));
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            AppUser admin = new AppUser(1, "test", passwordEncoder.encode("test"), roles);
            userRepository.save(admin);
        };
    }
}
