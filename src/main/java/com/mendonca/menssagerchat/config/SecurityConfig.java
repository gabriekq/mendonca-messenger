package com.mendonca.menssagerchat.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


@Configuration
public class SecurityConfig {
	
	@Autowired
	private MessageFilter messageFilter;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	
		http.csrf().disable()
		             .addFilterAfter( messageFilter , AuthorizationFilter.class)
		            .authorizeHttpRequests().antMatchers("/chat", "/message/send","/css/*","/javascript/*").authenticated()		          
					.antMatchers("/security/register").permitAll().and().formLogin().and().httpBasic();
		
		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
