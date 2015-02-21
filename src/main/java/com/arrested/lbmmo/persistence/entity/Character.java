package com.arrested.lbmmo.persistence.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Character {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="character_seq_gen")
	@SequenceGenerator(name="character_seq_gen", sequenceName="CHARACTER_ID")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;
	
	@OneToMany(mappedBy="character")
	private Set<QuestInProgress> questsInProgress;
	
	private String name;
	
	private boolean loggedIn;
	
	public Character() {
		
	}
	
	public Character(String name, User user) {
		
		this.name = name;
		this.user = user;
		
		loggedIn = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setQuestsInProgress(Set<QuestInProgress> questsInProgress) {
		this.questsInProgress = questsInProgress;
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
