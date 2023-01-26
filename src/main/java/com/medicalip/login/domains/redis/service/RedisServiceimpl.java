package com.medicalip.login.domains.redis.service;

import com.medicalip.login.domains.commons.util.Constants;
import com.medicalip.login.domains.redis.dto.RedisRequest;
import com.medicalip.login.domains.redis.dto.RedisTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisServiceimpl implements RedisService{
    private final StringRedisTemplate redisTemplate;
    @Override
    public void setRedisRefreshToken(RedisTokenDTO redisTokenDTO) {
        String redisKey = Constants.REDIS_REFRESH_TOKEN_KEY + "-" + redisTokenDTO.getUserSeq();
        System.out.println("redisKey :: " + redisKey);

        //Redis 등록(RedisToken)
        redisTemplate.opsForValue().set(redisKey, redisTokenDTO.getRefreshToken());

        //만료시간 설정(1달)
        redisTemplate.expire(redisKey, 30, TimeUnit.DAYS);
    }

    @Override
    public void setRedisAccessToken(RedisTokenDTO redisTokenDTO) {
        String redisKey = Constants.REDIS_ACCESS_TOKEN_KEY + "-" + redisTokenDTO.getUserSeq();
        System.out.println("redisKey :: " + redisKey);
        //Redis 등록(AccessToken)
        redisTemplate.opsForValue().set(redisKey, redisTokenDTO.getAccessToken());

        //만료시간 설정(5분)
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
    }

    @Override
    public void delRedisToken(String redisKey) {
        redisTemplate.delete(redisKey);
    }

    @Override
    public Set<String> getAccessList() {
//        redisTemplate.keys(Constants.REDIS_ACCESS_TOKEN_KEY + "*");
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        Set<String> set = operations.getOperations().keys(Constants.REDIS_ACCESS_TOKEN_KEY + "*");
        System.out.println("set :: " + set);
//        return set;
        return redisTemplate.keys(Constants.REDIS_ACCESS_TOKEN_KEY + "*");
    }

    @Override
    public Set<String> getRefreshList() {
//        System.out.println("KEYS :: " + redisTemplate.keys(Constants.REDIS_REFRESH_TOKEN_KEY + "*"));
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        Set<String> set = operations.getOperations().keys(Constants.REDIS_REFRESH_TOKEN_KEY + "*");
        System.out.println("set :: " + set);
//        return set;
        return redisTemplate.keys(Constants.REDIS_REFRESH_TOKEN_KEY + "*");
    }
}
