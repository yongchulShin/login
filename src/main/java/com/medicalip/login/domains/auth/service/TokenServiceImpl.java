package com.medicalip.login.domains.auth.service;

import javax.transaction.Transactional;

import com.medicalip.login.domains.auth.repo.TokenRepository;
import com.medicalip.login.domains.commons.advice.exception.UnauthorizedException;
import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.redis.service.RedisService;
import com.medicalip.login.domains.match.repo.MatchUserRoleRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.users.entity.Users;
import com.medicalip.login.domains.users.repo.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
	private final TokenRepository tokenRepository;

	private final UsersRepository usersRepository;
	private final TokenUtils tokenUtils;
	private final MatchUserRoleRepository matchUserRoleRepository;
	private final RedisService redisService;
	@Override
	public TokenResponse generateJwtToken(String expiredAccessToken, String refreshToken) {
		// TODO Auto-generated method stub
//		Users users = usersRepository.findByUserEmail(email).get();
		TokenResponse tr = null;
		log.info("Token Service [generateJwtToken]");
		try{
			//토큰에서 userSeq 추출
			String userSeq = tokenUtils.getClaimsToken(refreshToken).getSubject();
			log.info("userSeq :: " + userSeq);
			if(userSeq == null)
				throw new IllegalArgumentException();

			//DB에 저장된 RefreshToken과 Request RefreshToken 비교
			String refreshTokenFromDb = getRefreshToken(userSeq);
			log.info("userSeq ::" + userSeq);
			log.info("refreshTokenFromDb :: " + refreshTokenFromDb);
			if(!refreshToken.equals(refreshTokenFromDb)){ // DB와 Request의 RefreshToken이 다르면 :: 권한이 없음
				throw new UnauthorizedException();
			}
			Optional<Users> users = usersRepository.findByUserSeq(Long.parseLong(userSeq));
			if(!users.isPresent())
				throw new NoSuchElementException();

			String newAccessToken = tokenUtils.generateJwtToken(users.get());

			tr = new TokenResponse(HttpStatus.OK, "ACCESS TOKEN을 재발행 하였습니다.", newAccessToken, refreshToken, matchUserRoleRepository.findByUsers(Long.parseLong(userSeq)));
			redisService.setRedis(tr);
			log.info("newAccessToken :: " + newAccessToken);
		}catch (ExpiredJwtException e){
			log.info("username from expired accessToken :: " + e.getClaims().getSubject());
			throw new UnauthorizedException();
		}
		return tr;
	}

	@Override
	public String getRefreshToken(String seq) {
		Optional<Users> users = usersRepository.findByUserSeq(Long.parseLong(seq));
		return tokenRepository.findByUsers(users.get()).getRefreshToken();
	}

}
