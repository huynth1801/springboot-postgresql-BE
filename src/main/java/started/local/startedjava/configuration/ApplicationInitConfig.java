package started.local.startedjava.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import started.local.startedjava.entity.ERole;
import started.local.startedjava.entity.User;
import started.local.startedjava.repository.UserRepository;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = Collections.singleton(ERole.ADMIN.name());

                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
//                user.setRoles(roles);

                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin. Please change it.");
            }
        };
    }
}
