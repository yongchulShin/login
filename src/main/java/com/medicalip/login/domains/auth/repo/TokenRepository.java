package com.medicalip.login.domains.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicalip.login.domains.auth.entity.Token;
import com.medicalip.login.domains.users.entity.Users;

public interface TokenRepository extends JpaRepository<Token, Long>{
	Optional<Token> findById(long id);
	
	Token findByUsers(Users users);
	
	Optional<Token> findByRefreshToken(String refreshToken);

}
