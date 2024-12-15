package com.mendonca.menssagerchat.logs;

import java.lang.reflect.Array;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.mendonca.menssagerchat.model.PayloadMessage;

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
