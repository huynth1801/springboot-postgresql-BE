package started.local.startedjava.entity.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "category")
public class Category extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail")
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category parentCategory;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
}
