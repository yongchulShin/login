package com.medicalip.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}
	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}
	
//	@Bean
//	CommandLineRunner run(UserService userService) {
//		return args -> {
//			userService.saveUserRole(new UserRole(null, "ROLE_USER"));
//			userService.saveUserRole(new UserRole(null, "ROLE_MANAGER"));
//			userService.saveUserRole(new UserRole(null, "ROLE_ADMIN"));
//		};
//	}

}
