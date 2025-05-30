package started.local.startedjava.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import started.local.startedjava.entity.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
