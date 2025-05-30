package started.local.startedjava.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
public class OrderVariantKey implements Serializable {
    @Column(name = "order_id", nullable = false)
    Long orderId;

    @Column(name = "variant_id", nullable = false)
    Long variantId;
}