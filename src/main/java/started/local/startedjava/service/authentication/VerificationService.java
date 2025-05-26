package started.local.startedjava.service.authentication;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import started.local.startedjava.constant.AppConstant;
import started.local.startedjava.dto.request.authentication.UserRequest;
import started.local.startedjava.entity.authentication.Role;
import started.local.startedjava.entity.authentication.User;
import started.local.startedjava.entity.authentication.Verification;
import started.local.startedjava.entity.authentication.VerificationType;
import started.local.startedjava.exception.VerificationException;
import started.local.startedjava.mapper.UserMapper;
import started.local.startedjava.repository.authentication.UserRepository;
import started.local.startedjava.repository.authentication.VerificationRepository;
import started.local.startedjava.repository.customer.CustomerRepository;
import started.local.startedjava.service.mail.EmailSenderService;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationService {
    UserRepository userRepository;
    VerificationRepository verificationRepository;
    UserMapper userMapper;
    EmailSenderService emailSenderService;
    CustomerRepository customerRepository;
    PasswordEncoder passwordEncoder;

    private Long generateTokenVerify(UserRequest userRequest) {
        if(userRepository.existsUserByUsername(userRequest.getUsername())) {
            throw new VerificationException("Username already exists");
        }

        // (2) Check if email existing in db
        if(userRepository.existsUserByEmail(userRequest.getEmail())) {
            throw new VerificationException("Email already exists");
        }

        // Create user entity with status 2 (non-verified) and set role Customer
        User user = new User();
        user.setStatus(2); // Non-verified
        user.setRoles(Set.of((Role) new Role().setId(3L)));

        userRepository.save(user);

        // (4) Create new verification entity and set user, token
        Verification verification = new Verification();
        String token = generateVerificationToken();

        verification.setUser(user);
        verification.setToken(token);
        verification.setExpiredAt(Instant.now().plus(5, ChronoUnit.MINUTES));
        verification.setType(VerificationType.REGISTRATION);

        verificationRepository.save(verification);

        // (5) Send email
        Map<String, Object> attributes = Map.of(
                "token", token,
                "link", MessageFormat.format("{0}/signup?userId={1}", AppConstant.FRONTEND_HOST, user.getId())
        );
        emailSenderService.sendVerificationToken(user.getEmail(), attributes);
        return user.getId();
    }

    private String generateVerificationToken() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }
}
