package com.arrested.lbmmo.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class SystemSetting {

	@Id
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

	public Integer getIntValue() {
		return intValue;
	}
	
	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	public Boolean getEnabled() {
		return enabled;
	}
}
