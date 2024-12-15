package com.mendonca.menssagerchat.controller.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.jms.JMSException;
import javax.jms.Connection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mendonca.menssagerchat.logs.AspectLogs;
import com.mendonca.menssagerchat.model.MessageManager;

@Component
public class ChatMendoncaBean {

	@Value("${mendonca.active.password}")
	private  String passwordActive;
	
	@Value("${mendonca.active.username}")
	private  String userNameActive;
	
	@Value("${mendonca.active.url}")
	private  String urlActive;
	
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


	@Bean
    public CacheManagerCustomizer <ConcurrentMapCacheManager> cacheManagerCustomizer(){
    	return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {

			@Override
			public void customize(ConcurrentMapCacheManager cacheManager) {
				cacheManager.setAllowNullValues(false);
			
			}
    		
    	};	
    }
	
	
	@Bean
	public AspectLogs CreateAspectLogs() {
		AspectLogs aspectLogs = new AspectLogs();
		return aspectLogs;
		
	}
	
	
}
