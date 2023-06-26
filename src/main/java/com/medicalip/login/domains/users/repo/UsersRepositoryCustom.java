package com.medicalip.login.domains.users.repo;

import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.dto.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsersRepositoryCustom {
    List<Users> findUser(UserRequest userRequest);
    Page<Users> findAll(Pageable pageable);

}
