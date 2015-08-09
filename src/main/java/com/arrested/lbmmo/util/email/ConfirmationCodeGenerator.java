package com.arrested.lbmmo.util.email;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import com.arrested.lbmmo.persistence.entity.User;

@Component
public class ConfirmationCodeGenerator {

	private MessageDigest hasher;
	
	public ConfirmationCodeGenerator() throws NoSuchAlgorithmException  {
		hasher = MessageDigest.getInstance("SHA-256");
	}
	
	public String getConfirmationCode(User user) {
		
		String hash = null;
		
		StringBuilder hashParts = new StringBuilder();
		hashParts.append(user.getUsername());
		hashParts.append(user.getEmail());
		
		if (user.getVerificationSent() != null) {
			hashParts.append(user.getVerificationSent().getTime());
		}
		
		try {
			byte[] bytes = hashParts.toString().getBytes("UTF-8");
			hash = String.format("%032X", new BigInteger(1, hasher.digest(bytes)));
		} catch (UnsupportedEncodingException e) {
			
		}
		
		return hash;
	}
}
