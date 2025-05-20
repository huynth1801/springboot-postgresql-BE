package started.local.startedjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    Permission findByName(String name);
}
