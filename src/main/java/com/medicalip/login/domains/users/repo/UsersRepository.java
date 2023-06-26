package com.medicalip.login.domains.users.repo;

import java.util.Optional;

import com.medicalip.login.domains.users.dto.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.medicalip.login.domains.users.entity.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, Long>{
	Optional<Users> findByUserEmail(String email);
	boolean existsByUserEmail(String email);

	Optional<Users> findByUserSeq(long userSeq);

}
