package app.seven.flexisafses.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.email")
@Component
public class EmailConfiguration {
    String host;
    int port;
    String username;
    String password;
}
