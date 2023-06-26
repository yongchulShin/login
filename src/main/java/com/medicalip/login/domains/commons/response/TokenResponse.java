package com.medicalip.login.domains.commons.response;

import com.medicalip.login.domains.match.entity.MatchUserRole;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TokenResponse {
	private int resultCode;
	private String resultMessage;
	private String accessToken;
	private String refreshToken;
//    private List<String> roles = new ArrayList<>();
	private List<MatchUserRole> matchUserRole;
	
	public TokenResponse(HttpStatus status, String message, String accessToken, String refreshToken, List<MatchUserRole> matchUserRole) {
		this.resultCode = status.value();
		this.resultMessage = message;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.matchUserRole = matchUserRole;
	}

	public TokenResponse(HttpStatus status, String message) {
		this.resultCode = status.value();
		this.resultMessage = message;
	}
}
