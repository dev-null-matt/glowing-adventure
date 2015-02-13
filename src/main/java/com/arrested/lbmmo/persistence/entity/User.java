package com.arrested.lbmmo.persistence.entity;

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
	
	private String password;
	
	private String email;
	
	@OneToMany(mappedBy="user")
	private Set<Character> characters;
	
	private String username;
	
	public long getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
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
