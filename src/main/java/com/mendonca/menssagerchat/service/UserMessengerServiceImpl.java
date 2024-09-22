package com.mendonca.menssagerchat.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.mendonca.menssagerchat.model.UserMessenger;
import com.mendonca.menssagerchat.repository.UserMessengerRepository;


@Service
public class UserMessengerServiceImpl implements UserMessengerService {

	
	@Autowired
     private UserMessengerRepository userMessengerRepository;
	
	@Override
	@Cacheable(cacheNames = "UserMessengerCache")
	public Optional<UserMessenger> searchByUserName(String userName) {

		Optional<UserMessenger> userMenssage = userMessengerRepository.findById(userName);	
		return userMenssage;
	}

}
