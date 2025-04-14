package started.local.startedjava.configuration;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import started.local.startedjava.entity.ERole;
import started.local.startedjava.entity.Role;
import started.local.startedjava.entity.User;
import started.local.startedjava.repository.RoleRepository;
import started.local.startedjava.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> runApplication(args.getSourceArgs(), userRepository, roleRepository);
    }

    @Transactional
    public void runApplication(String[] args, UserRepository userRepository, RoleRepository roleRepository) {
        // Ensure the ADMIN role exists in the database
        Optional<Role> adminRoleOptional = roleRepository.findByName(ERole.ADMIN);
        Role adminRole;

        if (adminRoleOptional.isPresent()) {
            adminRole = adminRoleOptional.get();
        } else {
            adminRole = new Role();
            log.info("Creating new role: {}", ERole.ADMIN);
            adminRole.setName(ERole.ADMIN);
            roleRepository.save(adminRole);
        }

        log.info("Admin role: {}", adminRole);
        log.info("Role name: {}", adminRole.getName());

        // Check if the admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("admin@admin.com");
            Set<Role> roles = Set.of(adminRole);
            user.setRoles(roles);

            userRepository.save(user);
            log.warn("Admin user has been created with default password: admin. Please change it.");
        }
    }
}
