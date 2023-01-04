package com.medicalip.login.domains.auth.service;

import org.springframework.http.ResponseEntity;

public interface TokenService {

	ResponseEntity<?> generateJwtToken(String email);

}
