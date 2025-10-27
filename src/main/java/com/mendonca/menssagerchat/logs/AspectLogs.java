package com.mendonca.menssagerchat.logs;

import java.lang.reflect.Array;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.mendonca.menssagerchat.controller.bean.ChatMendoncaBean;
import com.mendonca.menssagerchat.model.MessageManager;
import com.mendonca.menssagerchat.model.PayloadMessage;
import com.mendonca.menssagerchat.model.UserLoginMessage;

@Aspect
public class AspectLogs {

	private static final Logger LOGGER = LogManager.getLogger();

	@Before("execution(public * com.mendonca.menssagerchat.controller.MessageContoller.*(..))")
	private void printLogs(JoinPoint joinPoint) {

		String MethodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();

		LOGGER.info("MethodName: " + MethodName);
		evaluateArgs(args);

	}
	
	@AfterReturning("execution(public * com.mendonca.menssagerchat.controller.SecurityController.authenticateUser(..)) || execution(public * com.mendonca.menssagerchat.controller.SecurityController.logout(..))")
	private void sendSignalUpdateUserStatus(JoinPoint joinPoint) {
	
	String userName = evaluateParamsSendSignalUpdateUserStatus(joinPoint.getArgs());
	
		for(Entry<String, MessageManager> messageManager :ChatMendoncaBean.menssagesManager.entrySet()) {
		     
			if(!messageManager.getKey().equals(userName)) {
			 messageManager.getValue().updateUsersStatus();
			}
		}
	
	}

	private String evaluateParamsSendSignalUpdateUserStatus(Object... args) {
		
		int sizeArray=Array.getLength(args);
		
		if(sizeArray==0) {
			return "none";
		}else {
			Object object = Array.get(args, 0);
			UserLoginMessage userLoginMessage= (UserLoginMessage)object;
			String userName = userLoginMessage.getUsername();
			return userName;
		}
	}
	
	
	
	private void evaluateArgs(Object... args) {

		int size = Array.getLength(args);

		for (int index = 0; index < size; index++) {

			Object object = Array.get(args, index);
			parseObject(object);
		}

	}

	private static void parseObject(Object object) {

		if (object.getClass().equals(PayloadMessage.class)) {
			PayloadMessage payloadMessage = (PayloadMessage) object;
			LOGGER.info("Message Send from : " + payloadMessage.getSender() + " To : " + payloadMessage.getAddressee());

		} else {

			if (object.getClass().equals(String.class)) {
				String name = (String) object;
				LOGGER.info("String paramters :" + name);
			}
		}

	}

}
