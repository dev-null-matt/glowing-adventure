package com.arrested.lbmmo.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Waypoint {
	
	@Id
	@GeneratedValue
	private long id;
	private double latitude;
	private double longitude;
	private String description;
	
	public Long getId() {
	    return id;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public String getDescription() {
		return description;
	}
}
