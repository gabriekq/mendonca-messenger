package com.mendonca.menssagerchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mendonca.menssagerchat.model.UserMessenger;
import com.mendonca.menssagerchat.repository.UserMessengerRepository;

@RestController
@RequestMapping("security")
public class SecurityController {

	@Autowired
	private UserMessengerRepository userMessengerRepository;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
	
     @CrossOrigin(origins = "*")
	 @PostMapping("/register")
	    public ResponseEntity<String> registerUser(@RequestBody UserMessenger userMessenger) {
		 
		 if(!userMessengerRepository.existsById(userMessenger.getUserName())) {
	           
			  String hashPwd = passwordEncoder.encode(userMessenger.getPassword());
			  userMessenger.setPassword(hashPwd);
			 
			 userMessengerRepository.save(userMessenger);
			return ResponseEntity.status(HttpStatus.CREATED).body("Given user details are successfully registered");
			 
		 }else {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error to save the user");
		 }
		 
		 
		 
		 
	 }
	
	
	
	
}
