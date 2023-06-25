package com.test.menssagerchat;


import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mendonca.menssagerchat.config.UserDetailsConfig;
import com.mendonca.menssagerchat.model.UserMessenger;
import com.mendonca.menssagerchat.repository.UserMessengerRepository;


@ExtendWith(MockitoExtension.class)
class UserDetailsConfigTests {

	@InjectMocks
	private UserDetailsConfig userDetailsConfig;
	
	@Mock
	private UserMessengerRepository userMessengerRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder ;
	
	@Mock
	private Authentication authentication ;
	
	
	@Test
	void invalid_PassTest() {
    
		Optional<UserMessenger>  userMessenger  = createUserMessenger();
		
		Mockito.doReturn(userMessenger).when(userMessengerRepository).findById( Mockito.anyString() );
		Mockito.doReturn("Test").when(authentication).getName();
		Mockito.doReturn("12345").when(authentication).getCredentials();
		try {
			userDetailsConfig.authenticate(authentication);
		}catch (BadCredentialsException exception) {
		   Assertions.assertEquals("Invalid password!", exception.getMessage());
		}
		
	}
	
	@Test
	void authenticate_Test() {
		Optional<UserMessenger>  userMessenger  = createUserMessenger();
		
		Mockito.doReturn(userMessenger).when(userMessengerRepository).findById( Mockito.anyString() );
		Mockito.doReturn("Test").when(authentication).getName();
		Mockito.doReturn("12345").when(authentication).getCredentials();
		
		Mockito.doReturn(true).when(passwordEncoder).matches( Mockito.anyString(),  Mockito.anyString());
	    Authentication authenticateResult = userDetailsConfig.authenticate(authentication);
	    Assertions.assertNotNull(authenticateResult);
	}
	
	@Test
	void authenticate_notResgisterd() {
		Optional<UserMessenger>  userMessenger  = createUserMessengerEmpty();
		
		Mockito.doReturn(userMessenger).when(userMessengerRepository).findById( Mockito.anyString() );
		Mockito.doReturn("Test").when(authentication).getName();
		Mockito.doReturn("12345").when(authentication).getCredentials();
		
		try {
			userDetailsConfig.authenticate(authentication);
		}catch (BadCredentialsException exception) {
		   Assertions.assertEquals("No user registered with this details!", exception.getMessage());
		}
	}
	
	
	private  Optional<UserMessenger> createUserMessenger(){
		UserMessenger userMessenger = new UserMessenger();
		userMessenger.setPassword("12345");
		userMessenger.setUserName("Test");
		userMessenger.setRole("test");
		return Optional.of(userMessenger) ; 
	 }
	
	private  Optional<UserMessenger> createUserMessengerEmpty(){
		return Optional.empty();
	}
	

}
