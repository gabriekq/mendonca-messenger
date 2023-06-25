package com.mendonca.menssagerchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	
	@GetMapping(path = "/chat")
	public String loadChatPage() {
		return "conversation";
	}
	
	
	
	
}
