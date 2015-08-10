package com.arrested.lbmmo.ws.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.enitity.roles.UserRoleType;
import com.arrested.lbmmo.persistence.entity.Email;
import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.EmailRepository;
import com.arrested.lbmmo.persistence.repository.UserRepository;
import com.arrested.lbmmo.util.email.ConfirmationCodeGenerator;

@RestController
@RequestMapping("/service/account-settings/")
public class AccountSettingsController extends AbstractServiceController {

	@Autowired
	private ConfirmationCodeGenerator confirmationCodeGenerator;
	
	@Autowired
	private EmailRepository emailRepo;
	
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
		
		User user = getServiceUser();
		user.setEmail(email);
		user.removeRole(UserRoleType.VERIFIED);
		
		userRepo.save(user);
		
		return true;
	}
	
	@RequestMapping(value="isVerified", method=RequestMethod.GET)
	public boolean doesUserHaveRoleConfirmed() {
		return getServiceUser().hasRole(UserRoleType.VERIFIED);
	}
	
	@RequestMapping(value="requestConfirmEmail", method=RequestMethod.POST)
	@Transactional
	public boolean requestConfirmationEmail() {
		
		User user = getServiceUser();
		Date verificationQueued = new Date();
				
		user.setVerificationSent(verificationQueued);
		
		// Create and queue verification email
		Email email = new Email();
		email.setRecipient(user);
		email.setSubject("Glowing Adventure - Email Confirmation");
		email.setBody("Your verification code is: " + confirmationCodeGenerator.getConfirmationCode(user));
		email.setQueuedDate(verificationQueued);
		
		emailRepo.save(email);
		userRepo.save(user);
		
		return true;
	}
	
	@RequestMapping(value="confirmEmail", method={RequestMethod.GET, RequestMethod.POST})
	@Transactional
	public boolean confirmEmail(@RequestParam String confirmationCode) {
		
		User user = getServiceUser();
		
		if (confirmationCodeGenerator.getConfirmationCode(user).equals(confirmationCode)) {
			
			user.setVerificationSent(null);
			user.assignRole(UserRoleType.VERIFIED);
			userRepo.save(user);
			
			return true;
		}
		
		return false;
	}
}
