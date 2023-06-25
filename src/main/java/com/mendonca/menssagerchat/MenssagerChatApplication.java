package com.mendonca.menssagerchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages = "com.mendonca.menssagerchat.controller")
@ComponentScan(basePackages = {"com.mendonca.menssagerchat.config"})
@EnableJpaRepositories
public class MenssagerChatApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
		SpringApplication.run(MenssagerChatApplication.class, args);
	}

}
