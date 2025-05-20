package started.local.startedjava.repository.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import started.local.startedjava.entity.authentication.Verification;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long>, JpaSpecificationExecutor<Verification> {
    Optional<Verification> findByUserId(Long userId);
}
