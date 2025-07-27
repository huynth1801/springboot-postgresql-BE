package started.local.startedjava.entity.product;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import started.local.startedjava.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Entity
@Table(name = "brand")
public class Brand extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 35)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
}
