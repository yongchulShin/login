package com.medicalip.login.domains.redis.controller;

import com.medicalip.login.domains.commons.util.Constants;
import com.medicalip.login.domains.redis.dto.RedisRequest;
import com.medicalip.login.domains.redis.dto.RedisTokenDTO;
import com.medicalip.login.domains.users.dto.Users;
import com.medicalip.login.domains.users.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
	@Operation(summary = "Redis RefreshToken ??????", description = "Redis Data ??????(RefreshToken)")
	public CommonResult setRedisRefreshToken(@Parameter(name = "RedisRequest", schema = @Schema(required = true,example =
			"{\"userEmail\":\"\",\n"
			+"\"refreshToken\":\"\"\n"
			+"}"))
			@RequestBody RedisRequest req) {
		//???????????? Email??? Users ?????? ??????
		Users user = userService.findByEmail(req.getUserEmail()).get();

		//Parameter(RedisTokenDTO) ?????? ?????? ??? ??? Set
		RedisTokenDTO rtd = new RedisTokenDTO();
		rtd.setUserSeq(user.getUserSeq());
		rtd.setRefreshToken(req.getRefreshToken());

		//Set Redis Refresh Token
		redisService.setRedisRefreshToken(rtd);
		return responseSerivce.getSuccessResult();
	}
	@PostMapping("/setRedisAccessToken")
	@Operation(summary = "Redis AccessToken ??????", description = "Redis Data ??????(AccessToken)")
	public CommonResult setRedisAccessToken(@Parameter(name = "RedisRequest", schema = @Schema(required = true,example =
			"{\"userEmail\":\"\",\n"
			+"\"accessToken\":\"\"\n"
			+"}"))
			@RequestBody RedisRequest req) {
		//???????????? Email??? Users ?????? ??????
		Users user = userService.findByEmail(req.getUserEmail()).get();

		//Parameter(RedisTokenDTO) ?????? ?????? ??? ??? Set
		RedisTokenDTO rtd = new RedisTokenDTO();
		rtd.setUserSeq(user.getUserSeq());
		rtd.setAccessToken(req.getAccessToken());

		//Set Redis AccessToken
		redisService.setRedisAccessToken(rtd);
		return responseSerivce.getSuccessResult();
	}
	@DeleteMapping("/delRefreshToken")
	@Operation(summary = "Redis Data ??????(RefreshToken)", description = "Redis Data ??????(RefreshToken)")
	public CommonResult delRefreshToken(@Parameter(name = "RedisRequest", schema = @Schema(required = true,example =
			"{\"userEmail\":\"\"\n"
			+"}"))
			@RequestBody RedisRequest req) {
		Optional<Users> user = userService.findByEmail(req.getUserEmail());
		if(user.isPresent()){
			String refreshKey = Constants.REDIS_REFRESH_TOKEN_KEY+"-"+user.get().getUserSeq();
			redisService.delRedisToken(refreshKey);
		}
		return responseSerivce.getSuccessResult();
	}
	@DeleteMapping("/delAccessToken")
	@Operation(summary = "Redis DAta ??????(AccessToken)", description = "Redis Data ??????(AccessToken)")
	public CommonResult delRedis(@Parameter(name = "RedisRequest", schema = @Schema(required = true,example =
			"{\"userEmail\":\"\"\n"
			+"}"))
			@RequestBody RedisRequest req) {
		Optional<Users> user = userService.findByEmail(req.getUserEmail());
		if(user.isPresent()){
			String accessKey = Constants.REDIS_ACCESS_TOKEN_KEY+"-"+user.get().getUserSeq();
			redisService.delRedisToken(accessKey);
		}
		return responseSerivce.getSuccessResult();
	}
}
