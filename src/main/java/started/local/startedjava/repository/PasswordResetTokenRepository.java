package started.local.startedjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import started.local.startedjava.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
}
