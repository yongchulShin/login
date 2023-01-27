package com.medicalip.login.domains.redis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisRequest {
    private String userEmail;
    private String accessToken;
    private String refreshToken;
}

