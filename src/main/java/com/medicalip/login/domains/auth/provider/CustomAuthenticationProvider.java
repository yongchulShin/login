package com.medicalip.login.domains.auth.provider;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.medicalip.login.domains.users.service.CustomUserDetailsService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private CustomUserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		// TODO Auto-generated method stub
		UserDetails userPrinciple = userDetailsService.loadUserByUsername(auth.getPrincipal().toString());
		if(auth.getCredentials().toString() == userPrinciple.getPassword()) {
			System.out.println("Credential Equals");
//			UsernamePasswordAuthenticationToken(userPrinciple, userPrinciple.getAuthorities());
		}else {
			throw new BadCredentialsException("Bad Credential");
		}
		return auth;
	}

	@Override
	public boolean supports(Class<?> auth) {
		// TODO Auto-generated method stub
		return true;
	}

}
