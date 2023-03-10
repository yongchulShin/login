package com.medicalip.login.domains.commons.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.medicalip.login.domains.auth.repo.TokenRepository;
import com.medicalip.login.domains.commons.jwt.JwtAuthenticationFilter;
import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.users.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	private final TokenUtils tokenUtils;
	private final TokenRepository tokenRepository;
	private final CustomUserDetailsService customUserDetailsService;
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println("[AuthenticationManagerBuilder configure]");
    	auth.userDetailsService(new UserDetailsService() {
    		@Override
    		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
    			return (UserDetails) customUserDetailsService.loadUserByUsername(username);
    		}
    	});
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	System.out.println("[HttpSecurity configure]");
    	
        http
        	.httpBasic().disable()
            // rest api????????? ???????????? ??????. ??????????????? ????????? ??? ????????? ??? ???????????? ??????????????? ??????.
            .cors().configurationSource(corsConfigurationSource())
            .and()
	            // rest api ????????? csrf ????????? ?????? ??????. disable
	            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // jwt token?????? ??????????????? ????????? ?????? ???????????? ?????? ??????.
            .and()
	            .authorizeRequests() // ?????? ??????????????? ?????? ???????????? ??????
	                .mvcMatchers("/", "/test/**", "/user/**", "/redis/**",
	                		"/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
	                		"/swagger-ui.html", "/webjars/**", "/swagger/**","/swagger-ui/**","/filedown/**" ).permitAll()
	                .anyRequest().authenticated()
          .and()
          .addFilterBefore(new JwtAuthenticationFilter(tokenUtils, tokenRepository),
                  UsernamePasswordAuthenticationFilter.class);
	                // ??? ??? ????????? ????????? ?????? ????????? ????????? ?????? ??????
        		
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
    
    // CORS ?????? ??????
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	System.out.println("[CorsConfigurationSource]");
        CorsConfiguration configuration = new CorsConfiguration();

//        configuration.addAllowedOrigin("*");
//        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
