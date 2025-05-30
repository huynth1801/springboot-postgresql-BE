package started.local.startedjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"started"})
@EntityScan(basePackages = {"started"})
@EnableJpaRepositories(basePackages = {"started"})
public class StartedjavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartedjavaApplication.class, args);
    }

}
