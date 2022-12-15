package com.medicalip.login.domains.auth.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.commons.response.SingleResult;
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
	public SingleResult generateJwtToken(String email) {
		// TODO Auto-generated method stub
		Users users = usersRepository.findByUserEmail(email).get();
		String accessToken = tokenUtils.generateJwtToken(users);
		return new SingleResult(200, "Access-Token 발행을 완료하였습니다.", accessToken);
	}

}
