package started.local.startedjava.dto.response.address;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    Long id;
    Instant createdAt;
    Instant updatedAt;
    @Nullable
    String line;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DistrictResponse {
        Long id;
        Instant createdAt;
        Instant updatedAt;
        String name;
        String code;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class WardResponse {
        Long id;
        Instant createdAt;
        Instant updatedAt;
        String name;
        String code;
    }
}
