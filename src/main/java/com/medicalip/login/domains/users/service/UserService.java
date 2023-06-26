package com.medicalip.login.domains.users.service;

import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.users.dto.LoginRequest;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.entity.UserRole;
import com.medicalip.login.domains.users.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

	Optional<Users> findByEmail(String email);
	Optional<Users> findBySeq(long seq);
	Users signUp(UserRequest userRequest);
	List<Users> findUsers();
	TokenResponse signIn(LoginRequest.Login loginRequest);
	UserRole saveUserRole(UserRole userRole);

    void newUserPromotion(UserRequest userRequest);

    void saveMatchUserRole(Users users);

	List<Users> getUserInfo();
}
