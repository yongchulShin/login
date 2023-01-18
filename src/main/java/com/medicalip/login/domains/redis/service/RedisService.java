package com.medicalip.login.domains.redis.service;

import com.medicalip.login.domains.redis.dto.RedisRequest;
import com.medicalip.login.domains.redis.dto.RedisTokenDTO;

import java.util.Set;

public interface RedisService {
	void setRedisRefreshToken(RedisTokenDTO redisTokenDTO);

	void setRedisAccessToken(RedisTokenDTO redisTokenDTO);

	void delRedisAccessToken(String accessKey);

	Set<String> getAccessList();

	Set<String> getRefreshList();
}
