package started.local.startedjava.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import started.local.startedjava.repository.PasswordResetTokenRepository;
import started.local.startedjava.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
}
