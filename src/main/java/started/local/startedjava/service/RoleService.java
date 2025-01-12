package started.local.startedjava.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import started.local.startedjava.dto.request.RoleRequest;
import started.local.startedjava.dto.response.RoleResponse;
import started.local.startedjava.mapper.RoleMapper;
import started.local.startedjava.repository.PermissionRepository;
import started.local.startedjava.repository.RoleRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findByName(request.getPermissions().toString());
        log.info("service permisison {}", permissions);
        role.setPermissions((Collections.singleton(permissions)));
        role = roleRepository.save(role);
        log.info("Role created: {}", role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
