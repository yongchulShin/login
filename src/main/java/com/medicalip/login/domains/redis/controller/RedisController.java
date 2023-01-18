package com.medicalip.login.domains.redis.controller;

import com.medicalip.login.domains.commons.util.Constants;
import com.medicalip.login.domains.redis.dto.RedisRequest;
import com.medicalip.login.domains.redis.dto.RedisTokenDTO;
import com.medicalip.login.domains.users.dto.Users;
import com.medicalip.login.domains.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.*;

import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.response.SingleResult;
import com.medicalip.login.domains.commons.service.ResponseService;
import com.medicalip.login.domains.redis.service.RedisService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/redis")
@Tag(name = "2. Redis", description = "Redis Controller")
public class RedisController {

	@Autowired private RedisTemplate<String, String> redisTemplate;
	@Autowired private ResponseService responseSerivce;
	@Autowired private RedisService redisService;
	@Autowired private UserService userService;
	
	@PostMapping("/redisTest")
	public CommonResult addRedisKey(){
		ValueOperations<String, String> vop = redisTemplate.opsForValue();
		vop.set("test", "TEST");
		return responseSerivce.getSuccessResult();
	}
	
	@GetMapping("/redisTest/{key}")
	public SingleResult<String> getRedisKey(@PathVariable String key){
		ValueOperations<String, String> vop = redisTemplate.opsForValue();
		String value = vop.get(key);
		return responseSerivce.getSingleResult(value);
	}
	@GetMapping("/getAccessList")
	public CommonResult getAccessList(){
		return responseSerivce.getSingleResult(redisService.getAccessList());
	}

	@GetMapping("/getRefreshList")
	public CommonResult getRefreshList(){
		return responseSerivce.getSingleResult(redisService.getRefreshList());
	}

	@PostMapping("/setRedisRefreshToken")
	@Operation(summary = "Redis RefreshToken 등록", description = "Redis Data 등록(RefreshToken)")
	public CommonResult setRedisRefreshToken(@RequestBody RedisRequest req) {
		//입력받은 Email로 Users 객체 생성
		Users user = userService.findByEmail(req.getUserEmail()).get();

		//Parameter(RedisTokenDTO) 객체 생성 및 값 Set
		RedisTokenDTO rtd = new RedisTokenDTO();
		rtd.setUserSeq(user.getUserSeq());

		//Set Redis Refresh Token
		redisService.setRedisRefreshToken(rtd);
		return responseSerivce.getSuccessResult();
	}
	@PostMapping("/setRedisAccessToken")
	@Operation(summary = "Redis AccessToken 등록", description = "Redis Data 등록(AccessToken)")
	public CommonResult setRedisAccessToken(@RequestBody RedisRequest req) {
		//입력받은 Email로 Users 객체 생성
		Users user = userService.findByEmail(req.getUserEmail()).get();

		//Parameter(RedisTokenDTO) 객체 생성 및 값 Set
		RedisTokenDTO rtd = new RedisTokenDTO();
		rtd.setUserSeq(user.getUserSeq());

		//Set Redis AccessToken
		redisService.setRedisAccessToken(rtd);
		return responseSerivce.getSuccessResult();
	}
	@DeleteMapping("/delRefreshToken")
	@Operation(summary = "Redis Data 삭제(RefreshToken)", description = "Redis Data 삭제(RefreshToken)")
	public CommonResult delRefreshToken(@RequestBody RedisRequest req) {
		Optional<Users> user = userService.findByEmail(req.getUserEmail());
		if(user.isPresent()){
			String refreshKey = Constants.REDIS_REFRESH_TOKEN_KEY+"-"+user.get().getUserSeq();
			redisService.delRedisAccessToken(refreshKey);
		}
		return responseSerivce.getSuccessResult();
	}
	@DeleteMapping("/delAccessToken")
	@Operation(summary = "Redis DAta 삭제(AccessToken)", description = "Redis Data 삭제(AccessToken)")
	public CommonResult delRedis(@RequestBody RedisRequest req) {
		Optional<Users> user = userService.findByEmail(req.getUserEmail());
		if(user.isPresent()){
			String accessKey = Constants.REDIS_ACCESS_TOKEN_KEY+"-"+user.get().getUserSeq();
			redisService.delRedisAccessToken(accessKey);
		}
		return responseSerivce.getSuccessResult();
	}
}
