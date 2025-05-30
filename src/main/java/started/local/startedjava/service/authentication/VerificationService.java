package started.local.startedjava.service.authentication;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import started.local.startedjava.constant.AppConstant;
import started.local.startedjava.dto.request.authentication.RegistrationRequest;
import started.local.startedjava.dto.request.authentication.UserRequest;
import started.local.startedjava.entity.authentication.Role;
import started.local.startedjava.entity.authentication.User;
import started.local.startedjava.entity.authentication.Verification;
import started.local.startedjava.entity.authentication.VerificationType;
import started.local.startedjava.entity.customer.Customer;
import started.local.startedjava.entity.customer.CustomerGroup;
import started.local.startedjava.entity.customer.CustomerResource;
import started.local.startedjava.entity.customer.CustomerStatus;
import started.local.startedjava.exception.VerificationException;
import started.local.startedjava.mapper.authentication.UserMapper;
import started.local.startedjava.repository.authentication.RoleRepository;
import started.local.startedjava.repository.authentication.UserRepository;
import started.local.startedjava.repository.authentication.VerificationRepository;
import started.local.startedjava.repository.customer.CustomerRepository;
import started.local.startedjava.service.mail.EmailSenderService;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class VerificationService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    VerificationRepository verificationRepository;
    UserMapper userMapper;
    EmailSenderService emailSenderService;
    CustomerRepository customerRepository;
    PasswordEncoder passwordEncoder;

    @Transactional
    public Long generateTokenVerify(UserRequest userRequest) {
        if(userRepository.existsUserByUsername(userRequest.getUsername())) {
            throw new VerificationException("Username already exists");
        }

        // (2) Check if email existing in db
        if(userRepository.existsUserByEmail(userRequest.getEmail())) {
            throw new VerificationException("Email already exists");
        }

        // Create user entity with status 2 (non-verified) and set role Customer
        User user = userMapper.toUser(userRequest);
        user.setStatus(2); // Non-verified
        Role role = roleRepository.findById(3L)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        user.setRoles(Set.of(role));
        log.info("User : {}", user);

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
        log.info("5. Saving verification and sending email...");
        emailSenderService.sendVerificationToken(user.getEmail(), attributes);
        log.info("6. Done!");
        return user.getId();
    }

    public void confirmVerification(RegistrationRequest registrationRequest) {
        Optional<Verification> verifyOpt = verificationRepository.findByUserId(registrationRequest.getUserId());
        if(verifyOpt.isPresent()) {
            Verification verification = verifyOpt.get();

            boolean validVerification = verification.getToken().equals(registrationRequest.getToken())
                    && verification.getExpiredAt().isBefore(Instant.now())
                    && verification.getType().equals(VerificationType.REGISTRATION);

            if(validVerification) {
                // Set status verified
                User user = verification.getUser();
                user.setStatus(1);
                userRepository.save(user);
                verificationRepository.delete(verification);

                Customer customer = new Customer();
                customer.setUser(user);
                customer.setCustomerGroup((CustomerGroup) new CustomerGroup().setId(1L));
                customer.setCustomerStatus((CustomerStatus) new CustomerStatus().setId(1L));
                customer.setCustomerResource((CustomerResource) new CustomerResource().setId(1L));
                customerRepository.save(customer);
            }

            boolean isTokenExpired = verification.getToken().equals(registrationRequest.getToken())
                    && verification.getExpiredAt().isAfter(Instant.now())
                    && verification.getType().equals(VerificationType.REGISTRATION);

            if(isTokenExpired) {
                String token = generateVerificationToken();
                verification.setToken(token);
                verification.setExpiredAt(Instant.now().plus(5, ChronoUnit.MINUTES));

                verificationRepository.save(verification);

                Map<String, Object> attributes = Map.of(
                        "token", token,
                        "link", MessageFormat.format("{0}/signup?userId={1}", AppConstant.FRONTEND_HOST, registrationRequest.getUserId()));
                emailSenderService.sendVerificationToken(verification.getUser().getEmail(), attributes);

                throw new VerificationException("Token is expired");
            }

            if (!verification.getToken().equals(registrationRequest.getToken())) {
                throw new VerificationException("Invalid token");
            }
        } else {
            throw new VerificationException("User does not exist");
        }
    }

    private String generateVerificationToken() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }
}
