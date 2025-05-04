package started.local.startedjava.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import started.local.startedjava.dto.request.RoleRequest;
import started.local.startedjava.dto.response.RoleResponse;
import started.local.startedjava.entity.authentication.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateFromRequest(RoleRequest request, @MappingTarget Role role);

}