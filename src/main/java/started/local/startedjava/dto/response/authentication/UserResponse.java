package started.local.startedjava.dto.response.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;
import started.local.startedjava.dto.response.RoleResponse;
import started.local.startedjava.dto.response.address.AddressResponse;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    Instant createdAt;
    Instant updatedAt;
    String username;
    String fullName;
    String email;
    String phone;
    String gender;
    AddressResponse address;
    String avatar;
    Integer status;
    Set<RoleResponse> roles;
}
