package com.medicalip.login.domains.users.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicalip.login.domains.users.dto.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
	Optional<Users> findByUserEmail(String email);
	boolean existsByUserEmail(String email);
}
