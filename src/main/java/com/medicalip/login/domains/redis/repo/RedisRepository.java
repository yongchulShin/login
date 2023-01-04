package com.medicalip.login.domains.redis.repo;

import org.springframework.data.repository.CrudRepository;

import com.medicalip.login.domains.redis.dto.RedisTokenDTO;

public interface RedisRepository extends CrudRepository<RedisTokenDTO, Long>{

}
