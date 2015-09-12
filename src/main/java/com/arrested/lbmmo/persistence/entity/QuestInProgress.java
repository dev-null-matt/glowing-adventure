package com.arrested.lbmmo.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class QuestInProgress { 
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="qip_seq_gen")
	@SequenceGenerator(name="qip_seq_gen", sequenceName="QUEST_IN_PROGRESS_ID")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="CHARACTER_ID")
	private Character character;
	
	@ManyToOne
	@JoinColumn(name="QUEST_ID")
	private Quest quest;
	
	private int currentStep;

	private boolean tracked;
	
	private Date startDate;
	
	private Date completedDate;
	
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
	
	public void isTracked(boolean tracked) {
		this.tracked = tracked;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public Objective getCurrentObjective() {
		
		for (Objective objective : quest.getObjectives()) {
			if (currentStep == objective.getQuestStep()) {
				return objective;
			}
		}
		
		return null;
	}
	
	public boolean isComplete() {
		
		return getCurrentObjective() == null;
	}
}
