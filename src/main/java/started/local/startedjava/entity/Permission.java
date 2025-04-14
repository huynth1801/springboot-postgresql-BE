package started.local.startedjava.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue
    @UuidGenerator
    String id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(length = 500)
    String description;
}
