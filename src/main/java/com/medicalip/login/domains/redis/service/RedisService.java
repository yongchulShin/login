package com.medicalip.login.domains.redis.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.medicalip.login.domains.redis.dto.RedisTokenDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisService {
	
	private final StringRedisTemplate redisTemplate;

	public void setRedis(RedisTokenDTO redisTokenDTO) {
		// TODO Auto-generated method stub
		System.out.println("Set Redis Service");
		String redisKey = "refreshToken-" + redisTokenDTO.getUserSeq();
		System.out.println("redisKey :: " + redisKey);
		
		//Redis 등록
		redisTemplate.opsForValue().set(redisKey, redisTokenDTO.getRefreshToken());
		
		//만료시간 설정
		redisTemplate.expire(redisKey, 30, TimeUnit.DAYS);
	}

}
