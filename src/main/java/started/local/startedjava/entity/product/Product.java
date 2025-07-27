package started.local.startedjava.entity.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import started.local.startedjava.entity.BaseEntity;
import started.local.startedjava.entity.general.Image;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "short_description")
    private String shortDescription;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @JsonBackReference
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @JsonBackReference
    private Supplier supplier;
}
