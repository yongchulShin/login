package com.medicalip.login.domains.users.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;

import com.medicalip.login.domains.commons.advice.exception.ExistingUserException;
import com.medicalip.login.domains.commons.advice.exception.RequestParameterException;
import com.medicalip.login.domains.commons.advice.exception.UnauthorizedEmailException;
import com.medicalip.login.domains.commons.advice.exception.UserExistsException;
import com.medicalip.login.domains.commons.service.ResponseService;
import com.medicalip.login.domains.redis.service.RedisService;
import com.medicalip.login.domains.match.repo.MatchUserRoleRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.users.dto.LoginRequest;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.entity.Users;
import com.medicalip.login.domains.users.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user")
@Tag(name = "1. Users", description = "Users Controller")
public class UserController {
	private final UserService userService; // user Service
	private final ResponseService responseService;
	private final RedisService redisService;
	private final MatchUserRoleRepository matchUserRoleRepository;

	@GetMapping("/welcome")
	public String welcome() {
		return "현재시간 :: " + LocalDateTime.now();
	}

	@PostMapping("/signUser")
	@Operation(summary = "회원가입", description = "회원가입을 한다.")
	public CommonResult signUp(
			@Parameter(name = "userRequest", schema = @Schema(required = true, example =
					"{\n"
					+ "\"nationCode\":\"\",\n"
					+ "\"userEmail\":\"\",\n"
					+ "\"userPw\":\"\",\n"
					+ "\"userName\":\"\",\n"
					+ "\"userNum\":\"\",\n"
					+ "\"institude\":\"\",\n"
					+ "\"isEducation\":\"\",\n"
					+ "\"isSubscribe\":\"\"\n"
					+ "}"))
					@RequestBody UserRequest userRequest)
	{
		if (userRequest.getUserEmail().equals("") || userRequest.getUserPw().equals("")
				|| userRequest.getNationCode().equals("") || userRequest.getUserName().equals("")) {
			throw new RequestParameterException();
		}

		if(userRequest.getIsEducation() == null || userRequest.getIsEducation().equals(""))
			userRequest.setIsEducation("N");

		Optional<Users> users = userService.findByEmail(userRequest.getUserEmail());
		if(users.isPresent()){
			if(users.get().getEnabled().equals("N"))
				throw new UnauthorizedEmailException();
			if(users.get().getEnabled().equals("Y"))
				throw new UserExistsException();
		}

		//회원가입
		Users user = userService.signUp(userRequest);
		log.info("user :: " + user);
		//권한부여
		userService.saveMatchUserRole(user);

		//프로모션 등록
		userService.newUserPromotion(userRequest);
		return responseService.getSingleResult(user);
	}
	
	@PostMapping("/signIn")
	@Operation(summary = "로그인", description = "로그인을 한다.")
	public ResponseEntity<?> signIn(@Parameter(name = "LoginRequest", schema = @Schema(required = true,example =
			"{\"userEmail\":\"\",\n"
			+"\"userPw\":\"\"\n"
			+"}"))
			@RequestBody LoginRequest.Login loginRequest, HttpServletRequest request, HttpSession session) {
		loginRequest.setIp(request.getRemoteAddr());
		
		TokenResponse jwtToken;
		jwtToken = userService.signIn(loginRequest);

		//Session 등록
		redisService.setRedis(jwtToken);

//		session.setAttribute("sessionInfo", jwtToken);
		return ResponseEntity.ok().body(jwtToken);
		
	}
	
	@GetMapping("/info")
	public ResponseEntity<List<Users>> findUser() {
	  return ResponseEntity.ok().body(userService.findUsers());
	}

	@Operation(summary = "이메일 중복체크", description = "입력한 이메일로 가입된 정보가 있는지 체크한다.")
	@GetMapping(value = "/chkEmail")
	public CommonResult chkEmail(
			@RequestParam(name = "email", required = true) String email) {
		int cnt = userService.findByEmail(email).stream().toArray().length;
		log.debug("cnt :: " + cnt);
		if(cnt == 0) {
			return responseService.getSuccessResult();
		}else {
//			return responseService.getFailResult();
			throw new ExistingUserException();
		}
	}
}
