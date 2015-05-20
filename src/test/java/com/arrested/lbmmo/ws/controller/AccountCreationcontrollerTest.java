package com.arrested.lbmmo.ws.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.UserRepository;

public class AccountCreationcontrollerTest {

	@Mock
	private UserRepository userRepo;
	
	@InjectMocks
	private AccountCreationController controller;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void isEmailRegisteredTest_positive() {
		
		String testEmail = "foo@bar.com";
		
		Mockito.when(userRepo.findByEmailIgnoreCase(Mockito.eq(testEmail))).thenReturn(Arrays.asList(new User()));
		
		Assert.assertTrue(controller.isEmailRegistered(testEmail));
	}
	
	@Test
	public void isEmailRegisteredTest_negative() {

		String testEmail = "foo@bar.com";
		
		Mockito.when(userRepo.findByEmailIgnoreCase(Mockito.eq(testEmail))).thenReturn(new ArrayList<User>());
		
		Assert.assertFalse(controller.isEmailRegistered(testEmail));
	}
	
	@Test 
	public void isLoginRegisteredTest_positive() {

		String testLogin = "foo.bar";
		
		Mockito.when(userRepo.findByUsernameIgnoreCase(Mockito.eq(testLogin))).thenReturn(Arrays.asList(new User()));
		
		Assert.assertTrue(controller.isLoginRegistered(testLogin));
	}
	
	@Test 
	public void isLoginRegisteredTest_negative() {

		String testLogin = "foo.bar";
		
		Mockito.when(userRepo.findByUsernameIgnoreCase(Mockito.eq(testLogin))).thenReturn(new ArrayList<User>());
		
		Assert.assertFalse(controller.isLoginRegistered(testLogin));
	}
}
