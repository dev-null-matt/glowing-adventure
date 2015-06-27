package com.arrested.lbmmo.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.repository.UserRepository;

@RestController
@RequestMapping("/service/account-settings/")
public class AccountSettingsController extends AbstractServiceController {

	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping(value="password", method=RequestMethod.PUT)
	@Transactional
	public boolean updatePassword(@RequestBody String password) {
		
		userRepo.setPassword(getServiceUser().getUsername(), password);
		
		return true;
	}
	
	@RequestMapping(value="email", method=RequestMethod.PUT)
	@Transactional
	public boolean updateEmail(@RequestBody String email) {
		
		userRepo.setEmail(getServiceUser().getUsername(), email);
		
		return true;
	}
}
