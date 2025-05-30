package started.local.startedjava.repository.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import started.local.startedjava.entity.authentication.RefreshToken;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, JpaSpecificationExecutor<RefreshToken> {
    Optional<RefreshToken> findByToken(String token);
}
