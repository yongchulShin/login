package com.medicalip.login.domains.users.service;

import com.medicalip.login.domains.users.dto.CustomUserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.medicalip.login.domains.users.entity.Users;
import com.medicalip.login.domains.users.repo.UsersRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, BadCredentialsException {
        Users users = usersRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        return new CustomUserDetails(users);
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Users users) {
        return new User(users.getUserEmail(), users.getUserPw(), new CustomUserDetails(users).getAuthorities());
    }
}
