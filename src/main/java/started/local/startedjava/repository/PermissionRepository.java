package started.local.startedjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import started.local.startedjava.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    Permission findByName(String name);
}
