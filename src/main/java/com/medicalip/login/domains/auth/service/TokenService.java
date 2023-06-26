package com.medicalip.login.domains.auth.service;

import com.medicalip.login.domains.commons.response.TokenResponse;

public interface TokenService {

	TokenResponse generateJwtToken(String expiredAccessToken, String refreshToken);
	String getRefreshToken(String email);

}
