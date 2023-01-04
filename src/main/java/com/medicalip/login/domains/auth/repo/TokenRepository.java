package com.medicalip.login.domains.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicalip.login.domains.auth.dto.Token;
import com.medicalip.login.domains.users.dto.Users;

public interface TokenRepository extends JpaRepository<Token, Long>{
	Optional<Token> findById(long id);
	
	Optional<Token> findByUsers(Users users);
	
	Optional<Token> findByRefreshToken(String refreshToken);

}
