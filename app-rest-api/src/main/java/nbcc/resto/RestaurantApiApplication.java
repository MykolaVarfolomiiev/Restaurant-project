package nbcc.resto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"nbcc.resto","nbcc.auth", "nbcc.common", "nbcc.email"})
@EnableJpaRepositories({"nbcc.auth.repository", "nbcc.resto.repository" })
@EntityScan({"nbcc.auth.entity", "nbcc.resto.entity"})
public class RestaurantApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApiApplication.class, args);
    }
}
