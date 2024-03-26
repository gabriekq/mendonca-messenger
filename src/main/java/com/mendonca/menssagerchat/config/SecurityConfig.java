package com.mendonca.menssagerchat.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


@Configuration
public class SecurityConfig {
		
	@Autowired
	private JwtRequestFilter jwtRequestFilter; 
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	
		http.requiresChannel(channel -> channel.anyRequest().requiresSecure())
		    .csrf()
		    .disable()   
		    .addFilterBefore(jwtRequestFilter, AuthorizationFilter.class)
		    .authorizeHttpRequests()
		    .antMatchers("/message/send")
		    .authenticated()
			.antMatchers("/security/register","/css/*","/authenticate","/chat").permitAll().and().formLogin().loginPage("/myLogin").and().sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
