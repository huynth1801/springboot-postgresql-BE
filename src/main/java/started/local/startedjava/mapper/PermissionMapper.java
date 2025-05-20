package started.local.startedjava.mapper;

import org.mapstruct.Mapper;
import started.local.startedjava.dto.request.PermissionRequest;
import started.local.startedjava.dto.response.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
