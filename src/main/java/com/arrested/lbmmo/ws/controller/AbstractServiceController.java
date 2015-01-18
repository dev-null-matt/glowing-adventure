package com.arrested.lbmmo.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.UserRepository;

@RequestMapping("service")
public abstract class AbstractServiceController {

	@Autowired
	private UserRepository userRepository;
	
	public User getServiceUser() {
		
		List<User> users = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		return users.isEmpty() ? null : users.get(0);
	}
}
