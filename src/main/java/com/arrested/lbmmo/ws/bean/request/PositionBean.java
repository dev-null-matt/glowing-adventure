package com.arrested.lbmmo.ws.bean.request;

import java.io.Serializable;

public class PositionBean implements Serializable {

	private static final long serialVersionUID = -1828499772489418619L;

	private double latitude;
	private double longitude;
	
	public PositionBean() {
		
	}
	
	public PositionBean(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}
