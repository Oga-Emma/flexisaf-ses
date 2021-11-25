package app.seven.flexisafses.repositories;

import app.seven.flexisafses.config.MongoValidationConfig;
import app.seven.flexisafses.models.pojo.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

//@DataMongoTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(MongoValidationConfig.class)
class StudentRepoTest {
    @Autowired
    private StudentRepo repository;

    @Test
    void shouldFailOnInvalidEntity() {
        Student invalidDocument = new Student();

        Assertions.assertThrows(ConstraintViolationException.class, () -> repository.save(invalidDocument));
    }

    @TestConfiguration
    static class MongoMapKeyDotReplacementConfiguration {
        @Bean
        public LocalValidatorFactoryBean localValidatorFactoryBean() {
            return new LocalValidatorFactoryBean();
        }
    }
}
