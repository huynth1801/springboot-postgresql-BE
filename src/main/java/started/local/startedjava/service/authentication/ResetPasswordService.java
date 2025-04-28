package started.local.startedjava.service.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import started.local.startedjava.entity.PasswordResetToken;
import started.local.startedjava.entity.User;
import started.local.startedjava.exception.AppException;
import started.local.startedjava.exception.ErrorCode;
import started.local.startedjava.repository.PasswordResetTokenRepository;
import started.local.startedjava.repository.UserRepository;
import started.local.startedjava.service.mail.EmailSenderService;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    public String requestResetPassword(String email) {
        var user  = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var oldResetTokenOpt = passwordResetTokenRepository.findByUser(user);

        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusSeconds(900);

        PasswordResetToken resetToken;

        if (oldResetTokenOpt.isPresent()) {
            // ✅ Nếu đã có token cũ, UPDATE lại
            resetToken = oldResetTokenOpt.get();
            resetToken.setToken(token);
            resetToken.setExpiryTime(expiry);
        } else {
            // ✅ Nếu chưa có, CREATE mới
            resetToken = PasswordResetToken.builder()
                    .user(user)
                    .token(token)
                    .expiryTime(expiry)
                    .build();
        }

        passwordResetTokenRepository.save(resetToken);

        String link = "http://localhost:8080/reset-password?token=" + token;

        Map<String, Object> model = Map.of(
                "username", user.getUsername(),
                "link", link
        );
        emailSenderService.sendForgetPasswordToken(user.getEmail(), model);
        return link;
    }

    public void resetPassword(String token, String newPassword) {
        var resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.OTP_INVALID));

        if (resetToken.getExpiryTime().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        User user = resetToken.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}
