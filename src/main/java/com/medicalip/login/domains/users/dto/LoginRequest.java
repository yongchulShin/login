package com.medicalip.login.domains.users.dto;

import java.security.NoSuchAlgorithmException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.medicalip.login.domains.commons.util.EncryptUtil;

import lombok.Getter;
import lombok.Setter;

public class LoginRequest {
	@Getter
    @Setter
    public static class Login {
        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String userEmail;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String userPw;
        
        private String serviceId;
        private String ip;
        
        public UsernamePasswordAuthenticationToken toAuthentication() {
        	UsernamePasswordAuthenticationToken authToken = null;
            try {
            	authToken =  new UsernamePasswordAuthenticationToken(userEmail, EncryptUtil.sha512(userPw));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return authToken;
        }
    }
}
