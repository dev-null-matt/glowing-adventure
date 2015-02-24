package com.arrested.lbmmo.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class QuestInProgress { 
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="CHARACTER_ID")
	private Character character;
	
	@ManyToOne
	@JoinColumn(name="QUEST_ID")
	private Quest quest;
	
	private int currentStep;

	private boolean tracked;
	
	public long getId() {
		return id;
	}

	public Character getCharacter() {
		return character;
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public void setQuest(Quest quest) {
		this.quest = quest;
	}
	
	public Quest getQuest() {
		return quest;
	}

	public int getCurrentStep() {
		return currentStep;
	}
	
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}
	
	public boolean isTracked() {
		return tracked;
	}
	
	public Objective getCurrentObjective() {
		
		for (Objective objective : quest.getObjectives()) {
			if (currentStep == objective.getQuestStep()) {
				return objective;
			}
		}
		
		return null;
	}
}
