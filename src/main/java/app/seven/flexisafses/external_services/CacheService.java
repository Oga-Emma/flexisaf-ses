package app.seven.flexisafses.external_services;

import app.seven.flexisafses.models.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CacheService {
    private final RedisTemplate<String, Student> studentRedisTemplate;

    public CacheService(@Autowired RedisTemplate<String, Student> studentRedisTemplate) {
        this.studentRedisTemplate = studentRedisTemplate;
    }

    public void setKey(Student student) {
        studentRedisTemplate.opsForValue().set(student.getMatricNumber(), student,
                Duration.ofHours(24));
    }

    public Student getRegistrationByKey(String key) {
        return studentRedisTemplate.opsForValue().get(key);
    }
}
