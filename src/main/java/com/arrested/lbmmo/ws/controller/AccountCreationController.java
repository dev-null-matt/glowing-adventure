package com.arrested.lbmmo.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.UserRepository;

@RestController
public class AccountCreationController extends AbstractServiceController {

	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping(value="accountCreation/isEmailRegistered/{email}", method=RequestMethod.GET)
	@Transactional
	public boolean isEmailRegistered(@PathVariable String email) {
		return ! userRepo.findByEmail(email).isEmpty();
	}
	
	@RequestMapping(value="accountCreation/isLoginRegistered/{login}", method=RequestMethod.GET)
	@Transactional
	public boolean isLoginRegistered(@PathVariable String login) {
		return ! userRepo.findByUsername(login).isEmpty();
	}
	
	@RequestMapping(value="accountCreation/create/{login}/{email}", method=RequestMethod.POST)
	@Transactional
	public boolean createUser(@PathVariable String login, @PathVariable String email, @RequestBody String password) {
		
		if (isLoginRegistered(login)) {
			return false;
		}
		
		if (isEmailRegistered(email)) {
			return false;
		}
		
		userRepo.createAccount(login, email, password);
		
		User newUser = userRepo.findByUsername(login).get(0);

		userRepo.giveUserRole(newUser.getId(), "USER");
		
		return true;
	}
}
