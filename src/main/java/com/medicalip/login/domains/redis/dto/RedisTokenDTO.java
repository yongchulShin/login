package com.medicalip.login.domains.redis.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisTokenDTO {
	
	@Id
	private long userSeq;
	private String refreshToken;
	private LocalDateTime expireTime;
	
}
