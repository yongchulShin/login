package com.medicalip.login.domains.redis.controller;

import com.medicalip.login.domains.commons.config.RedisConfig;
import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.service.ResponseService;
import com.medicalip.login.domains.redis.dto.RedisRequest;
import com.medicalip.login.domains.redis.service.RedisService;
import com.medicalip.login.domains.match.entity.MatchUserRole;
import com.medicalip.login.domains.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/redis")
public class RedisController {

	private final RedisConfig redisConfig;
	
	private final ResponseService responseSerivce;
	
	private final RedisService redisService;
	private final UserService userService;
	
	@PostMapping("/get")
	public CommonResult getRedis(@RequestBody RedisRequest request){
		MatchUserRole result = redisService.getRedis(userService.findByEmail(request.getEmail()).get().getUserSeq());
		log.info("result :: " + result);
		return responseSerivce.getSingleResult(result);
	}
	
//	@GetMapping("/redisTest/{key}")
//	public SingleResult<String> getRedisKey(@PathVariable String key){
//		ValueOperations<String, String> vop = redisTemplate.opsForValue();
//		String value = vop.get(key);
//		return responseSerivce.getSingleResult(value);
//	}
//
//	@PostMapping("/set")
//	public void setRedis(@RequestBody RedisTokenDTO redisTokenDTO) {
//		System.out.println("Set Redis Controller");
//		redisService.setRedis(redisTokenDTO);
//	}
}
