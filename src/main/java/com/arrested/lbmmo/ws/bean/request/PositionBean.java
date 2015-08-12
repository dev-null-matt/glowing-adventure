package com.arrested.lbmmo.ws.bean.request;

import java.io.Serializable;

public class PositionBean implements Serializable {

	private static final long serialVersionUID = 6346361044267664047L;

	private double latitude;
	private double longitude;
	private String description;
	
	public PositionBean() {
		
	}
	
	public PositionBean(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public PositionBean(double latitude, double longitude, String description) {
		this(latitude, longitude);
		this.description = description;
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
