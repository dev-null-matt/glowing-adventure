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
public class WaypointObjectiveType {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name="WAYPOINT_ID")
	private Waypoint waypoint;
	
	private int radius;
	
	@Enumerated(EnumType.STRING)
	private Objective.ObjectiveType objectiveType;

	public Waypoint getWaypoint() {
		return waypoint;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public Objective.ObjectiveType getObjectiveType() {
		return objectiveType;
	}
}