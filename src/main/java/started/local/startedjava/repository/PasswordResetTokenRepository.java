package started.local.startedjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import started.local.startedjava.entity.authentication.User;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
}
