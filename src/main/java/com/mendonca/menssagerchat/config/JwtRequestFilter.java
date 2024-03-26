package com.mendonca.menssagerchat.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mendonca.menssagerchat.model.UserMessenger;
import com.mendonca.menssagerchat.repository.UserMessengerRepository;
import com.mendonca.menssagerchat.util.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	 @Autowired
	 private UserMessengerRepository userMessengerRepository;
	
	
	@Autowired
	private JwtUtil jwtUtil;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader("Authorization");
		String jwt=null,username=null;
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
		
			jwt = authorizationHeader.substring(7);
			username = jwtUtil.extractUserName(jwt);
		}
		
		if((username != null) && (SecurityContextHolder.getContext().getAuthentication()!=null)) {
			Optional<UserMessenger> userMessenger = userMessengerRepository.findById(username);
		    
			if(jwtUtil.validateToken(jwt, userMessenger.get())) {
				
				 List<GrantedAuthority> authorities = new ArrayList<>();
	             authorities.add(new SimpleGrantedAuthority(userMessenger.get().getRole()));
				
				  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userMessenger.get().getUserName(), null, authorities );
				  usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}
			
		}
		filterChain.doFilter(request, response);
	}

	
}
