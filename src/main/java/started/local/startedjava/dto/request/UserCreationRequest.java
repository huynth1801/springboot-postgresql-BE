package started.local.startedjava.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 5, message = "USERNAME_INVALID")
    String  username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    String fullname;

    String email;

    String phone;

    String gender;

    String avatar;

    Integer status;
}
