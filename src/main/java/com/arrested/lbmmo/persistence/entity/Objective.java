package com.arrested.lbmmo.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Objective {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name="QUEST_ID")
	private Quest quest;
	
	@ManyToOne
	@JoinColumn(name="WAYPOINT_ID")
	private Waypoint waypoint;
	
	private int questStep;
	
	@Enumerated(EnumType.STRING)
	private ObjectiveType type;
	
	public enum ObjectiveType {
		QUEST_START,
		QUEST_WAYPOINT,
		QUEST_COMPLETION;
	}

	public long getId() {
		return id;
	}

	public Quest getQuest() {
		return quest;
	}

	public Waypoint getWaypoint() {
		return waypoint;
	}

	public int getQuestStep() {
		return questStep;
	}

	public ObjectiveType getType() {
		return type;
	}
}
