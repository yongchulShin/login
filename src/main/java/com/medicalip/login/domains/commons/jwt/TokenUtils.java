package com.medicalip.login.domains.commons.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.medicalip.login.domains.auth.dto.Token;
import com.medicalip.login.domains.commons.util.Constants;
import com.medicalip.login.domains.users.dto.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class TokenUtils {
	
	@Value("${spring.jwt.secret}") 
	String secretKey;
	
	public String generateJwtToken(Users users) {
		System.out.println("generateJwtToken");
		Claims claims = Jwts.claims().setSubject(users.getUserEmail()); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        claims.put("roles", users.getRoles()); // 정보는 key / value 쌍으로 저장된다.
	    return Jwts.builder()
	        .setSubject(users.getUserEmail())
	        .setHeader(createHeader())
//	        .setClaims(createClaims(users))
	        .setClaims(claims)
	        .setExpiration(createExpireDate(Constants.ACCESS_TOKEN_VALID_TIME))
	        .signWith(SignatureAlgorithm.HS512, createSigningKey(Constants.SECRET_KEY))
	        .compact();
	}
	
    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
    	System.out.println("getAuthentication accessToken :: " + accessToken);
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(Constants.AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(Constants.AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

	public String saveRefreshToken(Users users) {
		Claims claims = Jwts.claims().setSubject(users.getUserEmail()); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        claims.put("roles", users.getRoles()); // 정보는 key / value 쌍으로 저장된다.

		return Jwts.builder()
			.setSubject(users.getUserEmail())
			.setHeader(createHeader())
			.setClaims(createClaims(users))
			.setExpiration(createExpireDate(Constants.REFRESH_TOKEN_VALID_TIME))
			.signWith(SignatureAlgorithm.HS512, createSigningKey(Constants.REFRESH_KEY))
			.compact();
	}
	
	// Jwt 토큰으로 인증 정보 조회
//    public Authentication getAuthentication(Claims claims) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.get("sub").toString());
//        System.out.println("userDetails :: " + userDetails);
//        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//        System.out.println("UsernamePasswordAuthenticationToken :: " + upat);
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(Constants.SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
    
    // Request의 Header에서 token 값을 가져옵니다. "ACCESS_TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
	
	public String isValidToken(String token) {
		// TODO Auto-generated method stub
		token = token.replace("Bearer ", "");
		System.out.println("token :: " + token);
		try {
		  Claims accessClaims = getClaimsFormToken(token);
		  System.out.println("accessClaims is : " + accessClaims);
		  System.out.println("Access expireTime: " + accessClaims.getExpiration());
		  System.out.println("Access email: " + accessClaims.get("sub"));
//		  return !accessClaims.getExpiration().before(new Date());
		  return "success";
		} catch (ExpiredJwtException exception) {
		  System.out.println("Token Expired UserID : " + exception.getClaims().getSubject());
		  return "expired";
		} catch (JwtException exception) {
		  System.out.println("Token Tampered");
		  return "tampered";
		} catch (NullPointerException exception) {
		  System.out.println("Token is null");
		  return "null";
		}
	}

	public boolean isValidRefreshToken(String token) {
	    try {
	      Claims accessClaims = getClaimsToken(token);
	      System.out.println("accessClaims is : " + accessClaims);
	      System.out.println("refresh expireTime: " + accessClaims.getExpiration());
	      System.out.println("refresh email: " + accessClaims.get("sub"));
	      return true;
	    } catch (ExpiredJwtException exception) {
	      System.out.println("Token Expired UserID : " + exception.getClaims().getSubject());
	      return false;
	    } catch (JwtException exception) {
	      System.out.println("Token Tampered");
	      return false;
	    } catch (NullPointerException exception) {
	      System.out.println("Token is null");
	      return false;
	    }
	  }
	
	
	public Claims getClaimsToken(String token) {
		// TODO Auto-generated method stub
		return Jwts.parser()
		.setSigningKey(DatatypeConverter.parseBase64Binary(Constants.REFRESH_KEY))
		.parseClaimsJws(token)
		.getBody();
	}

	private Key createSigningKey(String key) {
		// TODO Auto-generated method stub
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
	    return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
	}

	public Date createExpireDate(long expireDate) {
		// TODO Auto-generated method stub
		long curTime = System.currentTimeMillis();
		System.out.println("curTime :: " + curTime);
		System.out.println("expireDate :: " + expireDate);
	    return new Date(curTime + expireDate);
	}

	private Map<String, Object> createClaims(Users users) {
		// TODO Auto-generated method stub
		Map<String, Object> claims = new HashMap<>();
	    claims.put(Constants.DATA_KEY, users.getUserEmail());
	    claims.put(Constants.AUTHORITIES_KEY, users.getRoles().getRoleName());
	    return claims;
	}

	private Map<String, Object> createHeader() {
		// TODO Auto-generated method stub
		Map<String, Object> header = new HashMap<>();

	    header.put("typ", "ACCESS_TOKEN");
	    header.put("alg", "HS512");
	    header.put("regDate", System.currentTimeMillis());

	    return header;
	}
	
	Claims getClaimsFormToken(String token) {
		// TODO Auto-generated method stub
		return Jwts.parser()
		        .setSigningKey(DatatypeConverter.parseBase64Binary(Constants.SECRET_KEY))
		        .parseClaimsJws(token)
		        .getBody();
	}

	// 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
	public Token generateToken(Authentication authentication) {
		// 권한 가져오기
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		String email = authentication.getName();
		System.out.println("generateToken email :: " + email);

		long now = (new Date()).getTime();
		// Access Token 생성
		Date accessTokenExpiresIn = createExpireDate(Constants.ACCESS_TOKEN_VALID_TIME);
		Date refreshTokenExpiresIn = createExpireDate(Constants.REFRESH_TOKEN_VALID_TIME);
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName())
				.claim(Constants.AUTHORITIES_KEY, authorities)
				.setExpiration(accessTokenExpiresIn)
				.signWith(SignatureAlgorithm.HS512, createSigningKey(Constants.SECRET_KEY))
				.compact();

		// Refresh Token 생성
		String refreshToken = Jwts.builder()
				.setSubject(authentication.getName())
				.setExpiration(refreshTokenExpiresIn)
				.signWith(SignatureAlgorithm.HS512, createSigningKey(Constants.REFRESH_KEY))
				.compact();

		return Token.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.refreshTokenExpireDt(refreshTokenExpiresIn)
				.build();
	}
	
	public String createRefreshToken () {
		long now = (new Date()).getTime();
		// Refresh Token 생성
		String refreshToken = Jwts.builder()
				.setExpiration(new Date(now + Constants.REFRESH_TOKEN_VALID_TIME))
				.signWith(SignatureAlgorithm.HS512, createSigningKey(Constants.REFRESH_KEY))
				.compact();
		
		return refreshToken;
	}
	
	private Claims parseClaims(String accessToken) {
				
        try {
            return Jwts.parser().setSigningKey(Constants.SECRET_KEY).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parser().setSigningKey(Constants.SECRET_KEY).parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
