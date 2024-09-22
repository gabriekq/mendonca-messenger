package com.mendonca.menssagerchat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	
	@GetMapping(path = "/chat")
	public String loadChatPage() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication.isAuthenticated());
		
		return "conversation";
	}
	
	
	@GetMapping(path = "/myLogin")
	public String loadMyLogin() {
		return "myLogin";
	}
	
}
