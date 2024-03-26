package com.mendonca.menssagerchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.mendonca.menssagerchat.config.UserDetailsConfig;
import com.mendonca.menssagerchat.model.UserLoginMessage;
import com.mendonca.menssagerchat.model.UserMessenger;
import com.mendonca.menssagerchat.repository.UserMessengerRepository;

@RestController
public class SecurityController {

	@Autowired
	private UserMessengerRepository userMessengerRepository;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
    @Autowired
    private UserDetailsConfig userDetailsConfig;
    
    
	
     @CrossOrigin(origins = "*")
	 @PostMapping("/security/register")
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
	
     
     @CrossOrigin(origins = "*")
	 @PostMapping("/authenticate")
     public ResponseEntity<?>  authenticateUser(@RequestBody UserLoginMessage userLoginMessage){
 
     try {
    	 userDetailsConfig.authenticate(new UsernamePasswordAuthenticationToken(userLoginMessage.getUsername(), userLoginMessage.getPassword())  );
     }catch (Exception exception) {
		System.out.println(exception.toString());		
     }
    	 
       return ResponseEntity.ok(userDetailsConfig.getJwtValue());
     }
     
	
	
	
}
