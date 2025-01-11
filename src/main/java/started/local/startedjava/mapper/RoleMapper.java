package started.local.startedjava.mapper;

import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import started.local.startedjava.dto.request.RoleRequest;
import started.local.startedjava.dto.response.PermissionResponse;
import started.local.startedjava.dto.response.RoleResponse;
import started.local.startedjava.entity.ERole;
import started.local.startedjava.entity.Permission;
import started.local.startedjava.entity.Role;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {
    // Chuyển từ RoleRequest -> Role
    public Role toRole(RoleRequest request) {
        if (request == null) return null;

        Role role = new Role();
        role.setName(ERole.valueOf(request.getName()));
        // Xử lý danh sách permissions từ request
        if (request.getPermissions() != null) {
            role.setPermissions(
                    request.getPermissions().stream()
                            .map(permissionName -> {
                                Permission permission = new Permission();
                                permission.setName(permissionName); // Chỉ gán name vì đây là @Id
                                return permission;
                            })
                            .collect(Collectors.toSet())
            );
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

