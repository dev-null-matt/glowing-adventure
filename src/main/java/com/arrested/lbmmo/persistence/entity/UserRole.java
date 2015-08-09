package com.arrested.lbmmo.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.arrested.lbmmo.persistence.enitity.roles.UserRoleType;

@Entity
@Table(name="USER_ROLE")
public class UserRole {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="user_role_seq_gen")
	@SequenceGenerator(name="user_role_seq_gen", sequenceName="USER_ROLE_ID")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;

	@Enumerated(EnumType.STRING)
	private UserRoleType role;

	public UserRole() {
		
	}
	
	public UserRole(User user, UserRoleType role) {
		this.user = user;
		this.role = role;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		UserRole other = (UserRole) obj;
		if (role != other.role)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
