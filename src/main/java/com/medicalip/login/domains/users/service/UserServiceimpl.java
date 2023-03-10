package com.medicalip.login.domains.users.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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
import com.medicalip.login.domains.redis.dto.RedisTokenDTO;
import com.medicalip.login.domains.redis.service.RedisService;
import com.medicalip.login.domains.users.dto.LoginRequest;
import com.medicalip.login.domains.users.dto.LoginRequest.Login;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.dto.UserRole;
import com.medicalip.login.domains.users.dto.Users;
import com.medicalip.login.domains.users.repo.UserRoleRepository;
import com.medicalip.login.domains.users.repo.UsersRepository;

import io.jsonwebtoken.Claims;
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
//	private final PasswordEncoder passwordEncoder; // ???????????? ?????????
	private final Response res;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final AuthenticationManager authenticationManager;
	private final RedisService redisService;
	
	public CommonResult signUp(UserRequest userRequest) {
		System.out.println("signUp Function");
		try {
			usersRepository.save(
		      Users.builder()
				.userEmail(userRequest.getUserEmail())
			    .userPw(EncryptUtil.sha512(userRequest.getUserPw()))
		        .userName(userRequest.getUserName())
		        .userNum(userRequest.getUserNum())
		        .institude(userRequest.getInstitude())
		        .enabled("N")
		        .isSubscribe(userRequest.getIsSubscribe())
		        .subscribeEmail(userRequest.getUserEmail())
	//	        .roles(Collections.singletonList(userRequest.getRole()))
		        .updDttm(LocalDateTime.now())
		        .regDttm(LocalDateTime.now())
		        .build());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CommonResult result = new CommonResult();
		result.setCode(200);
		result.setMsg("??????????????? ?????????????????????.");
		return result;
	}
	
	public TokenResponse signIn(LoginRequest.Login login) {
        try {
			System.out.println("[service sign in]");
        	Users users = usersRepository.findByUserEmail(login.getUserEmail()).get();
        	System.out.println("users :: " + users.getUserEmail());
        	System.out.println("users :: " + users.getRoles());
//        	tokenUtils.generateJwtToken(users);
        	// 1. Login ID/PW ??? ???????????? Authentication ?????? ??????
        	// ?????? authentication ??? ?????? ????????? ???????????? authenticated ?????? false
        	UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
        	System.out.println("authenticationToken :: " + authenticationToken);
        	
        	// 2. ?????? ?????? (????????? ???????????? ??????)??? ??????????????? ??????
            // authenticate ???????????? ????????? ??? CustomUserDetailsService ?????? ?????? loadUserByUsername ???????????? ??????
        	Authentication authentication = authenticationManager.authenticate(authenticationToken);
        	
            System.out.println("authentication :: " + authentication.getPrincipal());
        	// 3. ?????? ????????? ???????????? JWT ?????? ??????
        	Token tokenInfo = tokenUtils.generateToken(authentication);
        	Optional<Token> updateOp = tokenRepository.findByUsers(users);

        	// 4. RefreshToken ?????? (expirationTime ????????? ?????? ?????? ?????? ??????)
        	if(updateOp.stream().count() != 0) { 
        		System.out.println("Token Update");
        		Token updateToken = updateOp.get();
        		updateToken.setRefreshToken(tokenInfo.getRefreshToken());
        		updateToken.setAccessToken(tokenInfo.getAccessToken());
        		updateToken.setRefreshTokenExpireDt(tokenUtils.createExpireDate(Constants.REFRESH_TOKEN_VALID_TIME));

				RedisTokenDTO rtd = new RedisTokenDTO();
				rtd.setUserSeq(users.getUserSeq());
				rtd.setRefreshToken(updateToken.getRefreshToken());
				rtd.setAccessToken(updateToken.getAccessToken());

				//redis ??????
				System.out.println("updateToken :: " + updateToken);
				redisService.setRedisRefreshToken(rtd);
				redisService.setRedisAccessToken(rtd);
        		tokenRepository.save(updateToken);
        	}else {
        		System.out.println("?????? Login");
        		RedisTokenDTO rtd = new RedisTokenDTO();
        		rtd.setUserSeq(users.getUserSeq());
        		rtd.setRefreshToken(tokenInfo.getRefreshToken());
        		
        		//redis ??????
				redisService.setRedisRefreshToken(rtd);
				redisService.setRedisAccessToken(rtd);
        		
        		//db ??????
        		tokenRepository.save( // ??????(??????)
    				Token.builder()
        				.accessToken(tokenInfo.getAccessToken())
        				.refreshToken(tokenInfo.getRefreshToken())
        				.users(users)
        				.refreshTokenExpireDt(tokenUtils.createExpireDate(Constants.REFRESH_TOKEN_VALID_TIME))
        				.build());
        	}
        	
        	return TokenResponse.builder()
        			.status(HttpStatus.OK)
        			.message("???????????? ??????????????????.")
        			.accessToken(tokenInfo.getAccessToken())
        			.refreshToken(tokenInfo.getRefreshToken())
        			.rolesList(users.getRoles())
        			.build();
        }catch (BadCredentialsException e) {
        		return TokenResponse.builder()
        				.status(HttpStatus.FORBIDDEN)
        				.message("??????????????? ???????????? ????????????.")
        				.build();
    	}catch (NoSuchElementException e) {
    		return TokenResponse.builder()
    				.status(HttpStatus.BAD_REQUEST)
    				.message("???????????? ?????? ???????????????.")
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

	@Override
	public TokenResponse signInByRefreshToken(Login loginRequest, String refreshToken) {
		// TODO Auto-generated method stub
		System.out.println("[refreshToken] :: " + refreshToken);
		if(tokenUtils.isValidRefreshToken(refreshToken)) {
			Claims reClaims = tokenUtils.getClaimsToken(refreshToken);
			System.out.println("reClaims :: " + reClaims);
			if(reClaims.get("sub").equals(loginRequest.getUserEmail())) {
				try {
					Users users = usersRepository.findByUserEmail(loginRequest.getUserEmail()).get();
					String accessToken = tokenUtils.generateJwtToken(users);
					
					Token updateToken = tokenRepository.findByUsers(users).get();
					updateToken.setAccessToken(accessToken);
					updateToken.setUsers(users);
					tokenRepository.save(updateToken);

					RedisTokenDTO rtd = new RedisTokenDTO();
					rtd.setUserSeq(users.getUserSeq());
					rtd.setRefreshToken(updateToken.getRefreshToken());
					rtd.setAccessToken(updateToken.getAccessToken());

					//redis ??????
					redisService.setRedisRefreshToken(rtd);
					redisService.setRedisAccessToken(rtd);

					return TokenResponse.builder()
							.status(HttpStatus.OK)
							.message("???????????? ??????????????????.")
							.accessToken(updateToken.getAccessToken())
							.refreshToken(updateToken.getRefreshToken())
							.rolesList(users.getRoles())
							.build();
				}catch (BadCredentialsException e) {
					// TODO: handle exception
					return TokenResponse.builder()
							.status(HttpStatus.FORBIDDEN)
							.message("??????????????? ???????????? ????????????.")
//            			.accessToken(tokenInfo.getAccessToken())
//            			.refreshToken(tokenInfo.getRefreshToken())
							.build();
				}catch (NoSuchElementException e) {
					// TODO: handle exception
					return TokenResponse.builder()
							.status(HttpStatus.BAD_REQUEST)
							.message("???????????? ?????? ???????????????.")
//            			.accessToken(tokenInfo.getAccessToken())
//            			.refreshToken(tokenInfo.getRefreshToken())
							.build();
				}
			}
		}
		return TokenResponse.builder()
				.status(HttpStatus.ACCEPTED)
				.message("????????? ????????? ????????????.")
				.build();
	}
	
}
