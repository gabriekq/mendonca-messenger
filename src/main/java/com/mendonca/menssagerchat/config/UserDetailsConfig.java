package com.mendonca.menssagerchat.config;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mendonca.menssagerchat.model.UserMessenger;
import com.mendonca.menssagerchat.repository.UserMessengerRepository;

@Service
public class UserDetailsConfig implements AuthenticationProvider  {

	 @Autowired
	 private UserMessengerRepository userMessengerRepository;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		Optional<UserMessenger> userMenssage = userMessengerRepository.findById(userName);
		
		 if(userMenssage.isPresent()) {
			 
			 if(passwordEncoder.matches(password, userMenssage.get().getPassword())) {
				 List<GrantedAuthority> authorities = new ArrayList<>();
	             authorities.add(new SimpleGrantedAuthority(userMenssage.get().getRole()));
	             return new UsernamePasswordAuthenticationToken(userName, password, authorities);
			 }else {
				  throw new BadCredentialsException("Invalid password!");
			 }

		 }else {
			 throw new BadCredentialsException("No user registered with this details!");
		 }
		

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
	 
	


}
