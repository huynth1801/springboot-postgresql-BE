package started.local.startedjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import started.local.startedjava.entity.authentication.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
