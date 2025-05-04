package started.local.startedjava.dto.request.address;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {
    String line;
    Long provinceId;
    Long districtId;
    Long wardId;
}
