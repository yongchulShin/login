package com.medicalip.login.domains.users.service;

import java.util.List;
import java.util.Optional;

import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.users.dto.LoginRequest;
import com.medicalip.login.domains.users.dto.LoginRequest.Login;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.dto.UserRole;
import com.medicalip.login.domains.users.dto.Users;

public interface UserService {

	Optional<Users> findByEmail(String email);
	CommonResult signUp(UserRequest userRequest);
	List<Users> findUsers();
	TokenResponse signIn(LoginRequest.Login loginRequest);
	UserRole saveUserRole(UserRole userRole);
	TokenResponse signInByRefreshToken(Login loginRequest, String refreshToken);
}
