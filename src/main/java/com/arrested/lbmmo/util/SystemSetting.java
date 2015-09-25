package com.arrested.lbmmo.util;

public class SystemSetting {

	private String name;
	
	private String stringValue;
	
	private Integer intValue;
	
	private Boolean enabled;

	public String getName() {
		return name;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String value) {
		this.stringValue = value;
	}
	
	public Integer getIntValue() {
		return intValue;
	}
	
	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
