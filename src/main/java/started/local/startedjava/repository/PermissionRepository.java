package started.local.startedjava.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import started.local.startedjava.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    Permission findByName(String name);
}
