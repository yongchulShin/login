package com.medicalip.login.domains.users.service;

import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.entity.Users;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserInfoService {
	@Transactional
	void updateUserInfo(UserRequest userRequest);

	StringBuffer downloadUserList();

	List<Users> searchUser(UserRequest userRequest);

	@Transactional
	void activationUser(UserRequest userRequest);
	@Transactional
	void resetPassword(UserRequest userRequest) throws NoSuchAlgorithmException;
	@Transactional
	void secessionUser(UserRequest userRequest);
	@Transactional
	void deleteUser(UserRequest userRequest);
}
