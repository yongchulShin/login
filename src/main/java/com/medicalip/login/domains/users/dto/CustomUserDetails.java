package com.medicalip.login.domains.users.dto;

import com.medicalip.login.domains.users.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Users users;

    public CustomUserDetails(Users users) {
        this.users = users;
    }

    public final Users getUsers(){
        return users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return users.getMatchUserRoles().stream().map(o -> new SimpleGrantedAuthority(o.getUserRoles().getRoleName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return users.getUserPw();
    }

    @Override
    public String getUsername() {
        return users.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
