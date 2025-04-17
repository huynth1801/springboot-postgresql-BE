package started.local.startedjava.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void update(String roleId, RoleRequest request) {
        log.info("Permissions from request: {}", request.getPermissions());
        var role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        roleMapper.updateFromRequest(request, role);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        log.info("Updating permissions for role with id {}", permissions);
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
    }
}
