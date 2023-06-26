package com.medicalip.login.domains.redis.service;

import com.medicalip.login.domains.commons.config.RedisConfig;
import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.match.entity.MatchUserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisServiceImpl implements RedisService{
    private final RedisConfig redisConfig;

    @Override
    public void setRedis(TokenResponse jwtToken) {
        String userSeq = String.valueOf(jwtToken.getMatchUserRole().get(0).getUsers().getUserSeq());
        String redisKey = "sessionInfo-" + userSeq;
        MatchUserRole matchUserRole = jwtToken.getMatchUserRole().get(0);
        redisConfig.redisTemplate().opsForValue().set(redisKey, matchUserRole);
        redisConfig.redisTemplate().expire(redisKey, 5, TimeUnit.MINUTES);
    }

    @Override
    public MatchUserRole getRedis(Long userSeq) {
        String redisKey = "sessionInfo-" + userSeq;
        log.info("[redisKey] :: " + redisKey);
        return redisConfig.redisTemplate().opsForValue().get(redisKey);
    }
}
