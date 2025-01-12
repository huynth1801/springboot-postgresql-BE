package started.local.startedjava.mapper;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import started.local.startedjava.dto.request.RoleRequest;
import started.local.startedjava.dto.response.PermissionResponse;
import started.local.startedjava.dto.response.RoleResponse;
import started.local.startedjava.entity.ERole;
import started.local.startedjava.entity.Permission;
import started.local.startedjava.entity.Role;
import started.local.startedjava.repository.PermissionRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RoleMapper {
    @Autowired
    PermissionRepository permissionRepository;
    // Chuyển từ RoleRequest -> Role
    public Role toRole(RoleRequest request) {
        if (request == null) return null;

        Role role = new Role();
        role.setName(ERole.valueOf(request.getName()));

        // Map permissions from request to Set<Permission>
        if (request.getPermissions() != null) {
            log.info("Get permissions {}", request.getPermissions());

            // Convert permission names to Set<Permission>
            Set<Permission> permissions = request.getPermissions().stream()
                    .map(permissionName -> {
                        Permission permission = new Permission();
                        permission.setName(permissionName); // Set the name
                        return permission;
                    })
                    .collect(Collectors.toSet());

            role.setPermissions(permissions);
        } else {
            role.setPermissions(Collections.emptySet());
        }

        return role;
    }


    // Chuyển từ Role -> RoleResponse
    public RoleResponse toRoleResponse(Role role) {
        if (role == null) return null;

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName(String.valueOf(role.getName()));
        // Ánh xạ danh sách permissions
        if (role.getPermissions() != null) {
            roleResponse.setPermissions(
                    role.getPermissions().stream()
                            .map(permission -> new PermissionResponse(
                                    permission.getName(),        // name là @Id
                                    permission.getDescription() // description là thông tin bổ sung
                            ))
                            .collect(Collectors.toSet())
            );
        } else {
            roleResponse.setPermissions(Collections.emptySet());
        }
        return roleResponse;
    }
}

