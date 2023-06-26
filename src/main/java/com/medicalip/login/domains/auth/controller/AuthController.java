package com.medicalip.login.domains.auth.controller;

import com.medicalip.login.domains.auth.dto.TokenRequest;
import com.medicalip.login.domains.auth.service.TokenService;
import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.users.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

	private final TokenService tokenService;
	private final UserService userService;
	private final TokenUtils tokenUtils;


	@PostMapping("/refresh")
	public TokenResponse generateJwtToken(@Parameter(name = "TokenRequest", schema = @Schema(required = true,example =
			"{\"refreshToken\":\"\"\n"
			+"}")) @RequestBody TokenRequest tokenRequest, HttpServletRequest req) {
		String expiredAccessToken = tokenUtils.resolveToken(req);
		expiredAccessToken = expiredAccessToken.replace("Bearer ", "");
		String refreshToken = tokenRequest.getRefreshToken();
		log.info("get Expired accessToken :: " + expiredAccessToken);
		log.info("refreshToken :: " + refreshToken);
		return tokenService.generateJwtToken(expiredAccessToken, refreshToken);
	}
	
}
