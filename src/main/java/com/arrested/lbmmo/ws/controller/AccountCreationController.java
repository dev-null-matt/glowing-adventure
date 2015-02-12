package com.arrested.lbmmo.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.repository.UserRepository;

@RestController
public class AccountCreationController extends AbstractServiceController {

	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping("accountCreation/isEmailRegistered/{email}")
	@Transactional
	public boolean isEmailRegistered(@PathVariable String email) {
		return ! userRepo.findByEmail(email).isEmpty();
	}
	
	@RequestMapping("accountCreation/isLoginRegistered/{login}")
	@Transactional
	public boolean isLoginRegistered(@PathVariable String login) {
		return ! userRepo.findByUsername(login).isEmpty();
	}
}
