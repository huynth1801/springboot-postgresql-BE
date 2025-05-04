package started.local.startedjava.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import started.local.startedjava.dto.request.UserCreationRequest;
import started.local.startedjava.dto.request.UserUpdateRequest;
import started.local.startedjava.dto.response.UserResponse;
import started.local.startedjava.entity.authentication.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}