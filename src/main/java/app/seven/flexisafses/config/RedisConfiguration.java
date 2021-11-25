package app.seven.flexisafses.config;

import app.seven.flexisafses.models.pojo.Student;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RedisConfiguration {
    public RedisConfiguration(@Autowired MemorystoreConfigutration configuration) {
        this.configuration = configuration;
    }

    MemorystoreConfigutration configuration;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<Object>();

        poolConfig.setMinIdle(10);
        poolConfig.setMaxIdle(30);
        poolConfig.setMaxTotal(30);

        JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder()
//            .usePooling()
//            .poolConfig(poolConfig)
                .build();

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(configuration.getHost(), configuration.getPort());
        redisConfig.setPassword(RedisPassword.of(configuration.getPassword()));

        return new JedisConnectionFactory(redisConfig, jedisConfig);
    }

    @Bean
    public RedisTemplate<String, Student> studentRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Student> template = new RedisTemplate<String, Student>();
        template.setConnectionFactory(redisConnectionFactory);

        return template;
    }
//
//    @Bean
//    @Primary
//    RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, String> template = new RedisTemplate<String, String>();
//        template.setConnectionFactory(redisConnectionFactory);
//
//        return template;
//    }
}
