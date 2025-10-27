package com.mendonca.menssagerchat.service;


import java.util.Map;
import java.util.Optional;

import com.mendonca.menssagerchat.model.UserMessenger;

public interface UserMessengerService {

	Optional<UserMessenger> searchByUserName(String userName);
	
	public Map<String,Boolean> retreaveUsersAvalible(String userName);
	
	public void alterCurrentStatus(String userName, Boolean status );
	
	public boolean saveUser( UserMessenger userMessenger);
	
}
