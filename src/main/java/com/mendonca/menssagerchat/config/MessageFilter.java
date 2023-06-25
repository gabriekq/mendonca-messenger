package com.mendonca.menssagerchat.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.mendonca.menssagerchat.controller.bean.ChatMendoncaBean;
import com.mendonca.menssagerchat.model.MessageManager;

@Component
public class MessageFilter extends OncePerRequestFilter /* implements Filter */ {

	@Autowired
	private BeanFactory beanFactory;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getServletPath().equals("/chat");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();

		if (!ChatMendoncaBean.menssagesManager.containsKey(userName)) {
			MessageManager menssage = beanFactory.getBean(MessageManager.class, userName);
			ChatMendoncaBean.menssagesManager.put(userName, menssage);
				// String url = ((HttpServletRequest)request).getRequestURL().toString();
		}

		filterChain.doFilter(request, response);
	}

}
