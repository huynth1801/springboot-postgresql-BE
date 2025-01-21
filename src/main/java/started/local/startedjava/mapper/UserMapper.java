package started.local.startedjava.mapper;


import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import started.local.startedjava.dto.request.UserCreationRequest;
import started.local.startedjava.dto.request.UserUpdateRequest;
import started.local.startedjava.dto.response.PermissionResponse;
import started.local.startedjava.dto.response.RoleResponse;
import started.local.startedjava.dto.response.UserResponse;
import started.local.startedjava.entity.ERole;
import started.local.startedjava.entity.Permission;
import started.local.startedjava.entity.Role;
import started.local.startedjava.entity.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public User toUser(UserCreationRequest request) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        // Map roles to RoleResponse
        if (user.getRoles() != null) {
            response.setRoles(
                    (java.util.Set<RoleResponse>) user.getRoles().stream()
                            .map(role -> {
                                RoleResponse roleResponse = new RoleResponse();
                                roleResponse.setName(role.getName().name());

                                // Map permissions to their names or PermissionResponse
                                if (role.getPermissions() != null) {
                                    roleResponse.setPermissions(
                                            role.getPermissions().stream()
                                                    .map(permission -> {
                                                        PermissionResponse permissionResponse = new PermissionResponse();
                                                        permissionResponse.setName(permission.getName());
                                                        permissionResponse.setDescription(permission.getDescription());
                                                        return permissionResponse;
                                                    })
                                                    .collect(Collectors.toSet())
                                    );
                                } else {
                                    roleResponse.setPermissions(Collections.emptySet());
                                }

                                return roleResponse;
                            })
                            .collect(Collectors.toSet())
            );
        } else {
            response.setRoles(Collections.emptySet());
        }

        return response;
    }


    public void updateUser(User user, UserUpdateRequest request) {
        if (user == null || request == null) {
            throw new IllegalArgumentException("User and request must not be null");
        }

        // Update password if provided
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }

        // Roles are ignored based on the requirement
    }
}
