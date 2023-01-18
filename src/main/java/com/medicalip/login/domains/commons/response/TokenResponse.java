package com.medicalip.login.domains.commons.response;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medicalip.login.domains.users.dto.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
	private int resultCode;
	private String resultMessage;
	private String accessToken;
	private String refreshToken;
	private UserRole roles;
	
	@Builder
	public TokenResponse(HttpStatus status, String message, String accessToken, String refreshToken, UserRole rolesList) {
		this.resultCode = status.value();
		this.resultMessage = message;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.roles = rolesList;
	}
	
}
