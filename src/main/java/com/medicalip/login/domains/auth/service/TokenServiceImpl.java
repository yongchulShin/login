package com.medicalip.login.domains.auth.service;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.commons.response.Response;
import com.medicalip.login.domains.users.dto.Users;
import com.medicalip.login.domains.users.repo.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
	
	private final UsersRepository usersRepository;
	private final TokenUtils tokenUtils;
	
	@Override
	public ResponseEntity<?> generateJwtToken(String email) {
		// TODO Auto-generated method stub
		Users users = usersRepository.findByUserEmail(email).get();
		String accessToken = tokenUtils.generateJwtToken(users);
		return new Response().success(accessToken, "Access-Token 발행을 완료하였습니다.", HttpStatus.OK);
	}

}
