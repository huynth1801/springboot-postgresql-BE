package started.local.startedjava.mapper;


import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import started.local.startedjava.dto.request.UserCreationRequest;
import started.local.startedjava.dto.response.UserResponse;
import started.local.startedjava.entity.User;

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
        response.setRoles(user.getRoles());
        return response;
    }
}
