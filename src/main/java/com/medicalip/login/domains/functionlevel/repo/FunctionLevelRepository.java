package com.medicalip.login.domains.functionlevel.repo;

import com.medicalip.login.domains.functionlevel.entity.FunctionLevel;
import com.medicalip.login.domains.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FunctionLevelRepository extends JpaRepository<FunctionLevel, String>{
}
