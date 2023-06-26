package com.medicalip.login.domains.users.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicalip.login.domains.users.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	UserRole findByRoleName(String role);

}
