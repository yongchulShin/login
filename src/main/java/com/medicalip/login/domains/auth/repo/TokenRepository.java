package com.medicalip.login.domains.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicalip.login.domains.auth.dto.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{
	Optional<Token> findById(long id);
	
	Optional<Token> findByAccessToken(String accessToken);
}
