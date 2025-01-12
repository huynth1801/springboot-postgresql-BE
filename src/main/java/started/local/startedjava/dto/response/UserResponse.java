package started.local.startedjava.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import started.local.startedjava.entity.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    Set<RoleResponse> roles;
}
