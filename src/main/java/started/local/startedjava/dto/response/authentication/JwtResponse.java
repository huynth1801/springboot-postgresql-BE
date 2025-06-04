package started.local.startedjava.dto.response.authentication;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtResponse {
    private String message;
    private String token;
    private String refreshToken;
    private Instant createdAt;
}
