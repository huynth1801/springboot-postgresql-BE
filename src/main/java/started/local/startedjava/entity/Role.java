package started.local.startedjava.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "roles")
@Slf4j
public class Role {
    @Id
    @GeneratedValue
    @UuidGenerator
    String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    ERole name;

    @Column(length = 500)
    String description;

    @ManyToMany(fetch = FetchType.LAZY)
    Set<Permission> permissions;
}
