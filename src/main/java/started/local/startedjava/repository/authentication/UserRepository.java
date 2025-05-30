package started.local.startedjava.repository.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import started.local.startedjava.entity.authentication.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    Optional<User> findByEmailAndResetPasswordToken(String email, String resetPasswordToken);
}
