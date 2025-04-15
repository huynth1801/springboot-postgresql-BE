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
        // Tạo tất cả các role có trong enum ERole nếu chưa có
        for (ERole role : ERole.values()) {
            roleRepository.findByName(role).orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName(role);
                log.info("Creating new role: {}", role);
                return roleRepository.save(newRole);
            });
        }

        // Lấy lại các role đã có
        Role adminRole = roleRepository.findByName(ERole.ADMIN).get();
        Role userRole = roleRepository.findByName(ERole.USER).get();

        log.info("Admin role: {}", adminRole);
        log.info("User role: {}", userRole);

        // Tạo admin nếu chưa tồn tại
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@admin.com");
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
            log.warn("Admin user has been created with default password: admin. Please change it.");
        }
    }
}
