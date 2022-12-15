package com.medicalip.login.domains.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicalip.login.domains.auth.service.TokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {

	@Autowired private final TokenService tokenService;
	
//	@PostMapping("/access/generate")
//	public SingleResult generateJwtToken(@RequestBody LoginRequest.Login loginRequest) {
//		return tokenService.generateJwtToken(loginRequest.getEmail());
//	}
	
}
