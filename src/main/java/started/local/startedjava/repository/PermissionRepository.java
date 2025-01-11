package started.local.startedjava.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import started.local.startedjava.entity.Permission;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    Permission findByName(String name);
}
