package com.mendonca.menssagerchat.service;

import java.util.Optional;

import com.mendonca.menssagerchat.model.UserMessenger;

public interface UserMessengerService {

	Optional<UserMessenger> searchByUserName(String userName);
	
}
