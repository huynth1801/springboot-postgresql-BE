package started.local.startedjava.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import started.local.startedjava.constant.SecurityConstants;
import started.local.startedjava.entity.authentication.Role;
import started.local.startedjava.repository.authentication.RoleRepository;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String ...args) {
        insertRoleIfNotExists(SecurityConstants.Role.ADMIN, "ADMIN");
        insertRoleIfNotExists(SecurityConstants.Role.EMPLOYEE, "EMPLOYEE");
        insertRoleIfNotExists(SecurityConstants.Role.CUSTOMER, "CUSTOMER");
    }

    private void insertRoleIfNotExists(String code, String name) {
        if (!roleRepository.existsByCode(code)) {
            Role role = new Role();
            role.setCode(code);
            role.setName(name);
            role.setStatus(1); // 1: active
            roleRepository.save(role);
        }
    }
}
