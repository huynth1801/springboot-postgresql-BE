package started.local.startedjava.service.authentication;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import started.local.startedjava.mapper.UserMapper;
import started.local.startedjava.repository.authentication.UserRepository;
import started.local.startedjava.repository.authentication.VerificationRepository;
import started.local.startedjava.service.mail.EmailSenderService;

@Service
@AllArgsConstructor
@Transactional
public class VerificationService {
    private UserRepository userRepository;
    private VerificationRepository verificationRepository;
    private UserMapper userMapper;
    private EmailSenderService emailSenderService;

}
