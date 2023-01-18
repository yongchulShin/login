package com.medicalip.login.domains.redis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medicalip.login.domains.commons.util.EncryptUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.security.NoSuchAlgorithmException;

@Getter
@Setter
public class RedisRequest {
    private String userEmail;
    private String accessToken;
    private String refreshToken;
}

