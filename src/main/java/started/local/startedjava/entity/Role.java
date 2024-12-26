package started.local.startedjava.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =AccessLevel.PRIVATE)
@Document("roles")
public class Role {
    @Id
    String id;

    ERole name;
}
