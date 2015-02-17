package com.arrested.lbmmo.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.UserRepository;
import com.arrested.lbmmo.util.ActiveUserService;

@RequestMapping("/service")
public abstract class AbstractServiceController {

	@Autowired
	private ActiveUserService activeUserService;
	
	@Autowired
	private UserRepository userRepository;
	
	public User getServiceUser() {
		
		return activeUserService.getActiveUser();
	}
}
