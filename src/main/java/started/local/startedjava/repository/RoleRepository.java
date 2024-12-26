package started.local.startedjava.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import started.local.startedjava.entity.ERole;
import started.local.startedjava.entity.Role;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
