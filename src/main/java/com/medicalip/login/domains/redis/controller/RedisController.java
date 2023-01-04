package com.medicalip.login.domains.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.response.SingleResult;
import com.medicalip.login.domains.commons.service.ResponseService;
import com.medicalip.login.domains.redis.dto.RedisTokenDTO;
import com.medicalip.login.domains.redis.service.RedisService;

@RestController
@RequestMapping(value = "/redis")
public class RedisController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private ResponseService responseSerivce;
	
	@Autowired
	private RedisService redisService; 
	
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
	
	@PostMapping("/set")
	public void setRedis(@RequestBody RedisTokenDTO redisTokenDTO) {
		System.out.println("Set Redis Controller");
		redisService.setRedis(redisTokenDTO);
	}
}
