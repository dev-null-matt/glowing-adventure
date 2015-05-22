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
	
	@Test
	public void createUserTest_happyPath() {
		
		long testId = 1337;
		
		String testLogin = "foo.bar";
		String testEmail = "foo@bar.com";
		String testPassword = "foobaz";
		
		User testUser = new User();
		testUser.setId(testId);
		
		Mockito.when(userRepo.findByUsernameIgnoreCase(Mockito.eq(testLogin))).thenReturn(new ArrayList<User>());
		Mockito.when(userRepo.findByEmailIgnoreCase(Mockito.eq(testEmail))).thenReturn(new ArrayList<User>());
		Mockito.when(userRepo.findByUsername(testLogin)).thenReturn(Arrays.asList(testUser));
		
		Assert.assertTrue(controller.createUser(testLogin, testEmail, testPassword));
		
		Mockito.verify(userRepo).findByUsernameIgnoreCase(testLogin);
		Mockito.verify(userRepo).findByEmailIgnoreCase(testEmail);
		Mockito.verify(userRepo).createAccount(testLogin, testEmail, testPassword);
		Mockito.verify(userRepo).giveUserRole(testId, "USER");
	}
	
	@Test
	public void createUserTest_duplicateLogin() {
		
		String testLogin = "foo.bar";
		String testEmail = "foo@bar.com";
		String testPassword = "foobaz";
		
		Mockito.when(userRepo.findByUsernameIgnoreCase(Mockito.eq(testLogin))).thenReturn(Arrays.asList(new User()));

		Assert.assertFalse(controller.createUser(testLogin, testEmail, testPassword));
		
		Mockito.verify(userRepo, Mockito.never()).findByEmailIgnoreCase(Mockito.anyString());
		Mockito.verify(userRepo, Mockito.never()).createAccount(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void createUserTest_duplicateEmail() {
		
		String testLogin = "foo.bar";
		String testEmail = "foo@bar.com";
		String testPassword = "foobaz";
		
		Mockito.when(userRepo.findByUsernameIgnoreCase(Mockito.eq(testLogin))).thenReturn(new ArrayList<User>());
		Mockito.when(userRepo.findByEmailIgnoreCase(Mockito.eq(testEmail))).thenReturn(Arrays.asList(new User()));

		Assert.assertFalse(controller.createUser(testLogin, testEmail, testPassword));
		
		Mockito.verify(userRepo, Mockito.never()).createAccount(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
}
