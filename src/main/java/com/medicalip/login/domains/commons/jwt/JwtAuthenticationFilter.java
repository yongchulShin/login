package com.medicalip.login.domains.commons.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final TokenUtils tokenUtils; // JWT 토큰을 생성 및 검증 모듈 클래스

    // Request로 들어오는 Jwt Token의 유효성을 검증 (jwtTokenProvider.validateToken)하는
    // filter를 filterChain에 등록한다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
    	System.out.println("[doFilter]");
    	
    	//1. Request Header 에서 JWT 토큰 추출
    	String accessToken = ((HttpServletRequest) request).getHeader("ACCESS_TOKEN");
		System.out.println("[AccessToken] :: " + accessToken);
		
		// 2. validateToken 으로 토큰 유효성 검사
        boolean isAccessTokenValid = accessToken != null && tokenUtils.isValidToken(accessToken);
        // resolveToken : Request의 Header에서 token 파싱
        if (isAccessTokenValid) {
        	System.out.println("AccessToken Valid True !! ");
            // validateToken : Jwt 토큰의 유효성 + 만료일자 확인
            Authentication auth = tokenUtils.getAuthentication(accessToken);
            // getAuthentication : Jwt 토큰으로 인증 정보 조회
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
