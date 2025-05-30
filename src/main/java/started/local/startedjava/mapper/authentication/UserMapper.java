package started.local.startedjava.mapper.authentication;

import org.mapstruct.*;
import started.local.startedjava.dto.request.UserCreationRequest;
import started.local.startedjava.dto.request.UserUpdateRequest;
import started.local.startedjava.dto.request.authentication.UserRequest;
import started.local.startedjava.dto.response.authentication.UserResponse;
import started.local.startedjava.entity.authentication.User;
import started.local.startedjava.utils.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MapperUtils.class})
public interface UserMapper {
    User toUser(UserRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @BeanMapping(qualifiedByName = "attachUser")
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @BeanMapping(qualifiedByName = "attachUser")
    @Mapping(source = "password", target = "password", qualifiedByName = "hashPassword",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(@MappingTarget User entity, UserRequest request);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    User partialUpdate(@MappingTarget User entity, Client)
}