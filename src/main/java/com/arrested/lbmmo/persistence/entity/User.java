package com.arrested.lbmmo.persistence.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="USER_ACCOUNT")
public class User {

	@Id
	@GeneratedValue
	private long id;
	
	private String email;
	
	@OneToMany(mappedBy="user")
	private Set<Character> characters;
	
	private String username;
	
	private Date verificationSent;
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setVerificationSent(Date sentDate) {
		this.verificationSent = sentDate;
	}
	
	public Date getVerificationSent() {
		return verificationSent;
	}
	
	public void setCharacters(Set<Character> characters) {
		this.characters = characters;
	}
	
	public Set<Character> getCharacters() {
		return characters;
	}
	
	public Character getLoggedInCharacter() {
		
		for (Character character : characters) {
			if (character.isLoggedIn()) {
				return character;
			}
		}
		
		return null;
	}
	
	public Character getCharacterByName(String name) {
		
		for (Character character : characters) {
			if (character.getName().equalsIgnoreCase(name)) {
				return character;
			}
		}
		
		return null;
	}
}
