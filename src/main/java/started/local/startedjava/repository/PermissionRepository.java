package started.local.startedjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import started.local.startedjava.entity.authentication.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    Permission findByName(String name);
}
