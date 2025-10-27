package com.mendonca.menssagerchat.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.mendonca.menssagerchat.model.UserMessenger;
import com.mendonca.menssagerchat.repository.UserMessengerRepository;


@Service
public class UserMessengerServiceImpl implements UserMessengerService {
	
	@Autowired
     private UserMessengerRepository userMessengerRepository;
	
	@Autowired
	@Qualifier("usersAvalibleMap")
	private Map<String, Boolean> usersAvalible;
	
	
	@Override
	@Cacheable(cacheNames = "UserMessengerCache")
	public Optional<UserMessenger> searchByUserName(String userName) {

		Optional<UserMessenger> userMenssage = userMessengerRepository.findById(userName);	
		return userMenssage;
	}

	@Override
	public boolean saveUser( UserMessenger userMessenger) {
		
		if(!userMessengerRepository.existsById(userMessenger.getUserName())) {
			userMessengerRepository.save(userMessenger);
			return true;
		}else {
			return false;
		}

	}

	
	@Override
	public Map<String,Boolean> retreaveUsersAvalible(String userName) {	
		Map<String, Boolean> usersAvalibleCopy =  new LinkedHashMap<>(usersAvalible);
		usersAvalibleCopy.remove(userName);
		return usersAvalibleCopy ;	
	}
	
	@PostConstruct
	private void initUsersAvailable() {
		
	ArrayList<String> usersAvailable = new ArrayList<>(userMessengerRepository.findAllUsersById()); 
	
		for(String userAvailable :usersAvailable ) {
		
			if(!usersAvalible.containsKey(userAvailable)) {
				usersAvalible.put(userAvailable, Boolean.FALSE);
			}
		}
	}
	
	@Override
	public void alterCurrentStatus(String userName, Boolean status ) {
		usersAvalible.put(userName, status);
	} 
	
	
}
