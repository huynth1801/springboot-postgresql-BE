package started.local.startedjava.dto.request.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;
import started.local.startedjava.dto.request.address.AddressRequest;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String username;
    String password;
    String fullname;
    String email;
    String phone;
    String gender;
    AddressRequest address;
    String avatar;
    Integer status;
    Set<Role_UserRequest> roles;
}
