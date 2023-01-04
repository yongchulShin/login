package com.medicalip.login.domains.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private String redisPort;
	
	@Value("${spring.redis.password}")
	private String redisPassword;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
//		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//		config.setHostName(redisHost);
//		config.setPort(Integer.parseInt(redisPort));
//		config.setPassword(redisPassword);
//		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(config);
//		return connectionFactory;
		return new LettuceConnectionFactory(redisHost, Integer.parseInt(redisPort));
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(){
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(this.redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}

}
