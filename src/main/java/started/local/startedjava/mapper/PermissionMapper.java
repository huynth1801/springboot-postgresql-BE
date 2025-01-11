package started.local.startedjava.mapper;

import org.springframework.stereotype.Component;
import started.local.startedjava.dto.request.PermissionRequest;
import started.local.startedjava.dto.response.PermissionResponse;
import started.local.startedjava.entity.Permission;

@Component
public class PermissionMapper {
    public Permission toPermission(PermissionRequest request) {
        if(request == null) return null;

        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        return permission;
    }

    public PermissionResponse toPermissionResponse(Permission permission) {
        if(permission == null) return null;

        PermissionResponse permissionResponse = new PermissionResponse();
        permissionResponse.setName(permission.getName());
        permissionResponse.setDescription(permission.getDescription());
        return permissionResponse;
    }
}
