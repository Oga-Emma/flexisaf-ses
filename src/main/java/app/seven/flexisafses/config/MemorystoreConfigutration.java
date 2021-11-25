package app.seven.flexisafses.config;


import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.memstore")
@Component
public class MemorystoreConfigutration {
    String host;
    int port;
    String password;
}
