package com.medicalip.login.domains.redis.service;

import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.match.entity.MatchUserRole;

public interface RedisService {
    void setRedis(TokenResponse jwtToken);
    MatchUserRole getRedis(Long userSeq);
}
