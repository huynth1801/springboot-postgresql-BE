package started.local.startedjava.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import started.local.startedjava.dto.request.UserCreationRequest;
import started.local.startedjava.dto.response.UserResponse;
import started.local.startedjava.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

}
