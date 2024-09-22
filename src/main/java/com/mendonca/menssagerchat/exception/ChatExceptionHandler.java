package com.mendonca.menssagerchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ChatExceptionHandler extends ResponseEntityExceptionHandler  {

	
	@ExceptionHandler({ChatException.class})
	public ResponseEntity<String> userNotAuthenticate(Exception exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
	}	
		
}
