package com.medicalip.login.domains.users.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.users.dto.LoginRequest;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.dto.Users;
import com.medicalip.login.domains.users.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user")
@Tag(name = "1. Users", description = "Users Controller")
public class UserController {
	
	private final UserService userService; // user Service
	
	@PostMapping("/signup")
	@Operation(summary = "회원가입", description = "회원가입을 한다.")
	public CommonResult signUp(
//			@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = UserRequest.class))) 
								@RequestBody UserRequest userRequest) {
		CommonResult result = new CommonResult();
		  result.setCode(400);
		  result.setMsg("이미 가입된 회원입니다.");
	  return userService.findByEmail(userRequest.getUserEmail()).isPresent()
	      ? result
	      : userService.signUp(userRequest);
	}
	
	@PostMapping("/signin")
	@Operation(summary = "로그인", description = "로그인을 한다.")
	public ResponseEntity<?> signIn(@Parameter(name = "LoginRequest", schema = @Schema(required = true,example =
			"{\"userEmail\":\"\",\n"
			+"\"userPw\":\"\"\n"
			+"}"))
			@RequestBody LoginRequest.Login loginRequest, HttpServletRequest request,  HttpServletResponse response) {
		loginRequest.setIp(request.getRemoteAddr());
		
		TokenResponse jwtToken;
		System.out.println("request.getCookies() :: " + request.getCookies());
		if(request.getCookies() != null) {
			String refreshToken = "";
			Cookie[] cookie =  request.getCookies();
			
			for(int i = 0; i < cookie.length ; i++) {
				System.out.println("Cookie");
				if(cookie[i].getName().equals("refreshToken")){
					System.out.println("refreshToken not null");
					refreshToken = cookie[i].getValue();
				}
			}
			
			System.out.println("refreshToken :: " + refreshToken);
			if(refreshToken != null) {
				//refreshToken 있으므로 accessToken 재발행
				jwtToken = userService.signInByRefreshToken(loginRequest, refreshToken);
				return ResponseEntity.ok().body(jwtToken);
			}
		}
		
		jwtToken = userService.signIn(loginRequest);
		
		//쿠키생성
		Cookie refreshCookie = new Cookie("refreshToken", jwtToken.getRefreshToken());
		refreshCookie.setMaxAge(365 * 24 * 60 * 60); //만료기간 1년
		refreshCookie.setSecure(true);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setPath("/");
		
		response.addCookie(refreshCookie);
		return ResponseEntity.ok().body(jwtToken);
		
	}
	
	@GetMapping("/info")
	public ResponseEntity<List<Users>> findUser() {
	  return ResponseEntity.ok().body(userService.findUsers());
	}
	
//	@GetMapping("/chPass")
//	public String chPass() {
//		String pass = null;
//		try {
//			pass = EncryptUtil.sha512("1234");
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return pass;
//	}
}
