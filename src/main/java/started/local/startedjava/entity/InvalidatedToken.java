package started.local.startedjava.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "invalidated_tokens")
public class InvalidatedToken {

    @Id
    @GeneratedValue
    @UuidGenerator
    String id;

    @Temporal(TemporalType.TIMESTAMP)
    Date expiryTime;
}
