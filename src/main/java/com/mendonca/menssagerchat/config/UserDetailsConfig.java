package com.mendonca.menssagerchat.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.BeanFactory;
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

import com.mendonca.menssagerchat.controller.bean.ChatMendoncaBean;
import com.mendonca.menssagerchat.model.MessageManager;
import com.mendonca.menssagerchat.model.UserMessenger;
import com.mendonca.menssagerchat.service.UserMessengerService;
import com.mendonca.menssagerchat.util.JwtUtil;

@Service
public class UserDetailsConfig implements AuthenticationProvider {

	@Autowired
	private UserMessengerService userMessengerService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private BeanFactory beanFactory;
	
	private String jwtValue;
	

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		Optional<UserMessenger> userMenssage = userMessengerService.searchByUserName(userName);

		if (userMenssage.isPresent()) {

			if (passwordEncoder.matches(password, userMenssage.get().getPassword())) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority(userMenssage.get().getRole()));
				String jwt = jwtUtil.generateToken(userMenssage.get());
				System.err.println(jwt);
				setJwtValue(jwt);
				createMenssagerManager(userName);
                userMessengerService.alterCurrentStatus(userName, Boolean.TRUE);
				return new UsernamePasswordAuthenticationToken(userName, password, authorities);
			} else {
				throw new BadCredentialsException("Invalid password!");
			}

		} else {
			throw new BadCredentialsException("No user registered with this details!");
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	private void createMenssagerManager(String userName) {

		if (!ChatMendoncaBean.menssagesManager.containsKey(userName)) {
			MessageManager menssage = beanFactory.getBean(MessageManager.class, userName);
			ChatMendoncaBean.menssagesManager.put(userName, menssage);
		}
	}

	public String getJwtValue() {
		return jwtValue;
	}

	public void setJwtValue(String jwtValue) {
		this.jwtValue = jwtValue;
	}
	
	
	

}
