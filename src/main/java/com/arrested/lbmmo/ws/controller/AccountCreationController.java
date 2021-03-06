package com.arrested.lbmmo.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.enitity.roles.UserRoleType;
import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.UserRepository;

@RestController
@RequestMapping(value="/service/account-creation/")
public class AccountCreationController {

	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping(value="isEmailRegistered/{email}", method=RequestMethod.GET)
	@Transactional
	public boolean isEmailRegistered(@PathVariable String email) {
		return ! userRepo.findByEmailIgnoreCase(email).isEmpty();
	}
	
	@RequestMapping(value="isLoginRegistered/{login}", method=RequestMethod.GET)
	@Transactional
	public boolean isLoginRegistered(@PathVariable String login) {
		return ! userRepo.findByUsernameIgnoreCase(login).isEmpty();
	}
	
	@RequestMapping(value="create/{login}/{email}", method=RequestMethod.PUT)
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
		newUser.assignRole(UserRoleType.USER);
		
		userRepo.save(newUser);
		
		return true;
	}
}
