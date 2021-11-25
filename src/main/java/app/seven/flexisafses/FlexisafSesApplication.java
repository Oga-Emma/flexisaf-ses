package app.seven.flexisafses;

import app.seven.flexisafses.models.pojo.AppUser;
import app.seven.flexisafses.services.auth_user.AuthUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableConfigurationProperties
@EnableMongoRepositories
@EnableMongoAuditing
@EnableAsync
@SpringBootApplication
public class FlexisafSesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlexisafSesApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AuthUserService userService, BCryptPasswordEncoder passwordEncoder){
        return args -> {
            if(userService.getUsers().isEmpty()) {
                userService.saveUser(new AppUser("admin@flexisaf.com", "123456", false, "ROLE_SUPER_ADMIN"));
            }
        };
    }
}
