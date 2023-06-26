package com.medicalip.login.domains.commons.config;

import com.medicalip.login.domains.match.entity.MatchUserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;

//    @Value("${spring.data.redis.password}")
//    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, Integer.parseInt(redisPort));
    }

    @Bean
    public RedisTemplate<String, MatchUserRole> redisTemplate(){
        RedisTemplate<String, MatchUserRole> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(this.redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setKeySerializer(new Jackson2JsonRedisSerializer(String.class));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MatchUserRole.class));
        return redisTemplate;
    }
}