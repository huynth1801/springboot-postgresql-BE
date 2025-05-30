package started.local.startedjava.repository.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import started.local.startedjava.entity.authentication.Role;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByCode(String code);
    Optional<Role> findByCode(String code);
}

