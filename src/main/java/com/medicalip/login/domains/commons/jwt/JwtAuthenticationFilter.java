package com.medicalip.login.domains.commons.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.medicalip.login.domains.auth.dto.Token;
import com.medicalip.login.domains.auth.repo.TokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final TokenUtils tokenUtils; // JWT 토큰을 생성 및 검증 모듈 클래스
	private final TokenRepository tokenRepository;
    // Request로 들어오는 Jwt Token의 유효성을 검증 (jwtTokenProvider.validateToken)하는
    // filter를 filterChain에 등록한다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
    	System.out.println("[doFilter]");
    	HttpServletResponse response1 = (HttpServletResponse) response;
        HttpServletRequest request1 = (HttpServletRequest) request;
        
    	//1. Request Header 에서 JWT 토큰 추출
    	String accessToken = tokenUtils.resolveToken((HttpServletRequest) request);
		System.out.println("[accessToken] :: " + accessToken);
		
		if(accessToken != null) {
//			filterChain.doFilter(request, response);
//			return;
			// 2. validateToken 으로 토큰 유효성 검사
			String isAccessTokenValid = tokenUtils.isValidToken(accessToken);
			// resolveToken : Request의 Header에서 token 파싱
			if (isAccessTokenValid.equals("success")) {
				System.out.println("AccessToken Valid True !! ");
				// validateToken : Jwt 토큰의 유효성 + 만료일자 확인
				Authentication auth = tokenUtils.getAuthentication(accessToken);
				System.out.println("[auth] :: " + auth);
				// getAuthentication : Jwt 토큰으로 인증 정보 조회
				SecurityContextHolder.getContext().setAuthentication(auth);
			}else if(isAccessTokenValid.equals("expired")) {
				System.out.println("request.getCookies() :: " + request1.getCookies());
				if(!(request1.getCookies() == null)) {
					System.out.println("!(request.getCookies() == null)");
					String refreshToken = "";
					Cookie[] cookie =  request1.getCookies();
					
					for(int i = 0; i < cookie.length ; i++) {
						refreshToken = cookie[i].getValue();
					}
					
					System.out.println("refreshToken :: " + refreshToken);
					Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
					System.out.println("token.stream().count() :: " + token.stream().count());
					if(token.stream().count() > 0) {
						//refreshToken 있으므로 accessToken 재발행
						String jwtToken = tokenUtils.generateJwtToken(token.get().getUsers());
						Authentication auth = tokenUtils.getAuthentication(jwtToken);
						System.out.println("[auth] :: " + auth);
						// getAuthentication : Jwt 토큰으로 인증 정보 조회
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
				}
			}
		}
        filterChain.doFilter(request, response);
    }
}
