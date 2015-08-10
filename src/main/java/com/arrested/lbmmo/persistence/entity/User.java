package com.arrested.lbmmo.persistence.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.arrested.lbmmo.persistence.enitity.roles.UserRoleType;

@Entity
@Table(name="USER_ACCOUNT")
public class User {

	@Id
	@GeneratedValue
	private long id;
	
	private String email;
	
	@OneToMany(mappedBy="user")
	private Set<Character> characters;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
	private Set<UserRole> roles;
	
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
	
	public void assignRole(UserRoleType role) {
		roles.add(new UserRole(this, role));
	}
	
	public boolean hasRole(UserRoleType role) {
		return roles.contains(new UserRole(this, role));
	}
	
	public void removeRole(UserRoleType role) {
		roles.remove(new UserRole(this, role));
	}
	
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
