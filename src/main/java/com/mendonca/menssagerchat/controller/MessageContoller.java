package com.mendonca.menssagerchat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mendonca.menssagerchat.controller.bean.ChatMendoncaBean;
import com.mendonca.menssagerchat.exception.ChatException;
import com.mendonca.menssagerchat.model.MessageManager;
import com.mendonca.menssagerchat.model.PayloadMessage;
import com.mendonca.menssagerchat.repository.UserMessengerRepository;

@RestController
@RequestMapping("message")
public class MessageContoller {

	@Autowired
	private ExecutorService executorService;
	
	@Autowired
	private UserMessengerRepository userMessengerRepository;

	@CrossOrigin(origins = "*")
	@PostMapping(path = "/send")
	public synchronized ResponseEntity<String> sendMenssage(@RequestBody PayloadMessage payloadMessage) {		   
		MessageManager menssageManager = ChatMendoncaBean.menssagesManager.get(payloadMessage.getSender());
		menssageManager.setPayloadMessageSend(payloadMessage);
		executorService.submit(menssageManager);
		return ResponseEntity.status(HttpStatus.CREATED).body("Menssage Sent");

	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/currentUser")
	public ResponseEntity<String>  getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
		String userName = authentication.getName();
		return  ResponseEntity.ok(userName);
	}
	
	
	@CrossOrigin(origins = "*")
	@GetMapping("/usersAvailable/{userName}")
	public ResponseEntity<List<String>>   getUsersAvailable(@PathVariable String userName  ) {
		
		validateUserAuthenticate(userName);
		
	    ArrayList<String> usersAvailable = new ArrayList<>(userMessengerRepository.findAllUsersById()); 
	    List<String> usersAvailableFilter = usersAvailable.stream().filter( user -> !user.equalsIgnoreCase(userName)).collect(Collectors.toList());
		return ResponseEntity.ok(usersAvailableFilter);
	}
	
	
	
	@CrossOrigin(origins = "*")
	@GetMapping("/retrieveMessages/{userName}")
	public List<PayloadMessage> retrieveMessages(@PathVariable String userName){
		
		validateUserAuthenticate(userName);
		
	  return ChatMendoncaBean.menssagesManager.get(userName).getMessages();
			
	}
	
	
	private void validateUserAuthenticate(String userName){
		if(!ChatMendoncaBean.menssagesManager.containsKey(userName)) {  
			throw new ChatException("User not Authenticate");
		}
		
		
	}
	
	
	
}
