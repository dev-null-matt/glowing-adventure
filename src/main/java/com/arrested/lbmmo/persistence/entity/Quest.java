package com.arrested.lbmmo.persistence.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.google.common.base.Objects;

@Entity
@Table
public class Quest {

	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	
	private Date expiration;

	@OneToMany(mappedBy="quest")
	@OrderBy("quest_step asc")
	private Set<Objective> objectives;
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public Date getExpiration() {
		return expiration;
	}
	
	public void setObjectives(Set<Objective> objectives) {
		this.objectives = objectives;
	}
	
	public Set<Objective> getObjectives() {
		return objectives;
	}
	
	@Override
	public boolean equals(Object o2) {
		
		if (o2 instanceof Quest) {
			
			Quest q2 = (Quest) o2;
			
			return  Objects.equal(id, q2.getId()) && Objects.equal(name, q2.getName());

		} else {
			
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(id, name);
	}
}