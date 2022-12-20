package com.medicalip.login.domains.users.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medicalip.login.domains.auth.dto.Token;
import com.medicalip.login.domains.auth.repo.TokenRepository;
import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.response.Response;
import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.commons.util.Constants;
import com.medicalip.login.domains.commons.util.EncryptUtil;
import com.medicalip.login.domains.users.dto.LoginRequest;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.dto.UserRole;
import com.medicalip.login.domains.users.dto.Users;
import com.medicalip.login.domains.users.repo.UserRoleRepository;
import com.medicalip.login.domains.users.repo.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceimpl implements UserService{
	
	private final TokenUtils tokenUtils;
	private final UsersRepository usersRepository;
	private final UserRoleRepository userRoleRepository;
	private final TokenRepository tokenRepository;
//	private final PasswordEncoder passwordEncoder; // 비밀번호 암호화
	private final Response res;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final AuthenticationManager authenticationManager;
	
	public CommonResult signUp(UserRequest userRequest) {
		System.out.println("signUp Function");
	  Users users = null;
	try {
		users = usersRepository.save(
	    		  Users.builder()
		    		.userEmail(userRequest.getUserEmail())
		            .userPw(EncryptUtil.sha512(userRequest.getUserPw()))
	                .userName(userRequest.getUserName())
	                .userNum(userRequest.getUserNum())
	                .institude(userRequest.getInstitude())
	                .enabled("N")
	                .isSubscribe(userRequest.getIsSubscribe())
	                .subscribeEmail(userRequest.getUserEmail())
//	                .roles(Collections.singletonList(userRequest.getRole()))
	                .updDttm(LocalDateTime.now())
	                .regDttm(LocalDateTime.now())
	                .build());
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	  String accessToken = tokenUtils.generateJwtToken(users);
	  String refreshToken = tokenUtils.saveRefreshToken(users);
	  
	  tokenRepository.save(
	      Token.builder()
	      	.users(users)
	      	.accessToken(accessToken)
	      	.refreshToken(refreshToken)
	      	.accessTokenExpireDt(tokenUtils.createExpireDate(Constants.ACCESS_TOKEN_VALID_TIME))
	      	.refreshTokenExpireDt(tokenUtils.createExpireDate(Constants.REFRESH_TOKEN_VALID_TIME))
	      	.build());
	  
	  tokenUtils.generateJwtToken(users);
	  return new CommonResult(200, "회원가입에 성공하였습니다.");
	}
	
	public TokenResponse signIn(LoginRequest.Login login) {
        try {
        	Users users = usersRepository.findByUserEmail(login.getUserEmail()).get();
        	System.out.println("users :: " + users.getUserSeq());
        	
    		// 1. Login ID/PW 를 기반으로 Authentication 객체 생성
    		// 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
    		UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
    		System.out.println("authenticationToken :: " + authenticationToken);
    		
    		// 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
    		// authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
    		Authentication authentication = authenticationManager.authenticate(authenticationToken);
    		
    		System.out.println("authentication :: " + authentication.getPrincipal());
    		// 3. 인증 정보를 기반으로 JWT 토큰 생성
    		Token tokenInfo = tokenUtils.generateToken(authentication);
    		
    		//등록된 토큰이 있는지 확인
        	Token token = tokenRepository.findByUsers(users).stream()
        					.sorted(Comparator.comparing(Token::getTokenId).reversed())
        					.findFirst()
        					.orElse(tokenInfo);
//        					.orElseThrow(() -> new IllegalArgumentException());
        	
        	System.out.println("token :: " + token.getAccessToken());
        	System.out.println("token valid :: " + tokenUtils.isValidToken(token.getAccessToken()));
        	
        	//토큰 없음
        	if(tokenRepository.findByUsers(users).isEmpty()) {
        		// 4. AccessToken & RefreshToken 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        		tokenRepository.save(
        				Token.builder()
        				.accessToken(tokenInfo.getAccessToken())
        				.refreshToken(tokenInfo.getRefreshToken())
        				.users(users)
        				.accessTokenExpireDt(tokenUtils.createExpireDate(Constants.ACCESS_TOKEN_VALID_TIME))
        		      	.refreshTokenExpireDt(tokenUtils.createExpireDate(Constants.REFRESH_TOKEN_VALID_TIME))
        				.build());
        		
        		return TokenResponse.builder()
            			.status(HttpStatus.OK)
            			.message("로그인에 성공했습니다.")
            			.accessToken(tokenInfo.getAccessToken())
            			.refreshToken(tokenInfo.getRefreshToken())
            			.serviceId(login.getServiceId())
            			.rolesList(users.getRoles())
            			.build();
        	}else {
        		System.out.println("Token 정보 있음");
        		//만료시간
        		LocalDateTime accessExpireDt = token.getAccessTokenExpireDt().toInstant()
							        		.atZone(ZoneId.systemDefault())
							        		.toLocalDateTime();
        		//현재시간
        		LocalDateTime now = LocalDateTime.now();
        		
        		//토큰 만료시간 확인
        		if(accessExpireDt.isAfter(now)) {
        			System.out.println("Access Token 만료안됨");
    				
    				System.out.println("authentication :: " + authentication.getPrincipal());
    				
    				// Refresh Token 생성
    				String refreshToken = tokenUtils.createRefreshToken();
    				token.refreshUpdate(refreshToken);
    				
    				return TokenResponse.builder()
    						.status(HttpStatus.OK)
    						.message("로그인에 성공했습니다.")
    						.accessToken(token.getAccessToken())
    						.refreshToken(refreshToken)
    						.serviceId(login.getServiceId())
    						.rolesList(users.getRoles())
    						.build();
        		}else {
        			System.out.println("Access Token 만료됨");
            		token.accessUpdate(tokenInfo.getAccessToken());
            		token.refreshUpdate(tokenInfo.getRefreshToken());
            		token.accessExpireUpdate(tokenInfo.getAccessTokenExpireDt());
            		token.refreshExpireUpdate(tokenInfo.getRefreshTokenExpireDt());
            		
            		return TokenResponse.builder()
    						.status(HttpStatus.OK)
    						.message("로그인에 성공했습니다.")
    						.accessToken(token.getAccessToken())
    						.refreshToken(token.getRefreshToken())
    						.serviceId(login.getServiceId())
    						.rolesList(users.getRoles())
    						.build();
        		}
        	}
        }catch (BadCredentialsException e) {
        		return TokenResponse.builder()
        				.status(HttpStatus.FORBIDDEN)
        				.message("비밀번호가 일치하지 않습니다.")
        				.build();
    	}catch (NoSuchElementException e) {
    		return TokenResponse.builder()
    				.status(HttpStatus.BAD_REQUEST)
    				.message("존재하지 않는 회원입니다.")
    				.build();
    	}
	}
        	
	public List<Users> findUsers() {
	  return usersRepository.findAll(Sort.by(Sort.Direction.DESC, "userSeq"));
	}
		
	public Optional<Users> findByEmail(String email) {
		return usersRepository.findByUserEmail(email);
	}

	public UserRole saveUserRole(UserRole userRole) {
		log.info("Saving new role {} to the db", userRole.getRoleName());
        return userRoleRepository.save(userRole);
	}
	
}
