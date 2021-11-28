package app.seven.flexisafses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class MongoValidationConfig {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

    @Bean
    AuditorAware<String> auditorProvider(){
       return new AuditorAware<String>() {
           @Override
           public Optional<String> getCurrentAuditor() {
               return Optional.ofNullable(SecurityContextHolder.getContext())
                       .map(context -> {
                           return context.getAuthentication();
                       }).filter(Authentication::isAuthenticated)
                       .map(Authentication::getName);
           }
       };
    }
}
