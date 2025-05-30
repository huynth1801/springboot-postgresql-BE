package started.local.startedjava.entity.customer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import started.local.startedjava.entity.BaseEntity;
import started.local.startedjava.entity.order.OrderResource;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "customer_resource")
public class CustomerResource extends BaseEntity {
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @OneToMany(mappedBy = "customerResource", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Customer> employees = new ArrayList<>();

    @OneToMany(mappedBy = "customerResource", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderResource> orderResources = new ArrayList<>();
}
