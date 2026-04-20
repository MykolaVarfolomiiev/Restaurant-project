package nbcc.resto;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"nbcc.resto", "nbcc.common", "nbcc.auth", "nbcc.email"})
@EnableJpaRepositories({"nbcc.resto.repository", "nbcc.auth.repository"})
@EntityScan({"nbcc.resto.entity", "nbcc.auth.entity"})
public class RestaurantWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantWebApplication.class, args);
    }
}