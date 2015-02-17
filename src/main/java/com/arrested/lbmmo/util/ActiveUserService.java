package com.arrested.lbmmo.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.UserRepository;

@Component
public class ActiveUserService {

	@Autowired
	private UserRepository userRepository;
	
	public User getActiveUser() {
		List<User> users = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		return users.isEmpty() ? null : users.get(0);
	}
}