package com.arrested.lbmmo.util.email;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arrested.lbmmo.persistence.entity.User;

public class ConfirmationorGeneratorTest {

	private ConfirmationCodeGenerator generator;
	
	@Before
	public void init() throws NoSuchAlgorithmException {
		generator = new ConfirmationCodeGenerator();
	}
	
	@Test
	public void smokeTest() {
		
		User user = new User();
		user.setEmail("foo@bar.com");
		user.setUsername("foobar");
		user.setVerificationSent(new Date());
		
		String code = generator.getConfirmationCode(user);
		
		Assert.assertEquals(code, generator.getConfirmationCode(user));
	}
	
	@Test
	public void getConfirmationCode_nullSafe() {
		
		User user = new User();
		
		String code = generator.getConfirmationCode(user);
		
		Assert.assertNotNull("Code was null", code);
		Assert.assertEquals(code, generator.getConfirmationCode(user));
	}
	
	@Test
	public void getConfirmationCode_testEmail() {
		
		User user = new User();
		user.setEmail("foo@bar.com");
		
		String code = generator.getConfirmationCode(user);

		user.setEmail("bat@bar.com");
		
		Assert.assertFalse(code.equals(generator.getConfirmationCode(user)));
	}
	
	@Test
	public void getConfirmationCode_testLogin() {
		
		User user = new User();
		user.setUsername("foobar");
		
		String code = generator.getConfirmationCode(user);

		user.setUsername("foobaz");
		
		Assert.assertFalse(code.equals(generator.getConfirmationCode(user)));
	}
	
	@Test
	public void getConfirmationCode_testSentDate() throws InterruptedException {
		
		User user = new User();
		user.setVerificationSent(new Date());
		
		String code = generator.getConfirmationCode(user);
		
		// Need to have at least one millisecond between digest creations
		Thread.sleep(1);
		
		user.setVerificationSent(new Date());
		
		Assert.assertFalse(code.equals(generator.getConfirmationCode(user)));
	}
}