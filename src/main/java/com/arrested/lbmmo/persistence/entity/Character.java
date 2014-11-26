package com.arrested.lbmmo.persistence.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Character {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;
	
	@OneToMany(mappedBy="character")
	private Set<QuestInProgress> questsInProgress;
	
	private String name;
	
	private boolean loggedIn;
	
	public String getName() {
		return this.name;
	}
	
	public Set<QuestInProgress> getQuestsInProgress() {
		return this.questsInProgress;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void isLoggedIn(boolean flag) {
		loggedIn = flag;
	}
	
	public QuestInProgress getTrackedQuestInProgress() {
		
		for (QuestInProgress qip : questsInProgress) {
			if (qip.isTracked()) {
				return qip;
			}
		}
		
		return null;
	}
}
