package com.mendonca.menssagerchat.controller.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Connection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mendonca.menssagerchat.model.MessageManager;

@Component
public class ChatMendoncaBean {

	private final String passwordActive = "gabriel";
	private final String userNameActive = "gabriel";
	private final String urlActive = "tcp://localhost:61616";
	
	public static Map<String, MessageManager> menssagesManager = new HashMap<>();

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public ExecutorService getExecutionService() {
		ExecutorService executorService = Executors.newFixedThreadPool(16);
		return executorService;
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Connection getConnectionActiveMQ() throws JMSException {
		ActiveMQConnectionFactory  factory = new ActiveMQConnectionFactory(userNameActive, passwordActive, urlActive);
		factory.setTrustAllPackages(true);
		Connection connection = factory.createConnection();
		connection.start();
		return connection;
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public MessageManager createMessageManager(String userNameActive) {
		MessageManager MessageManager = new MessageManager(userNameActive);
		return MessageManager;
	}


}
