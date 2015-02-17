package com.arrested.lbmmo.util;

public enum SystemSettings {

	ACCOUNT_MAX_CHARACTERS("ACCOUNT.MAX_CHARACTERS");
	
	private String settingName;
	
	private SystemSettings(String settingName) {
		this.settingName = settingName;
	}
	
	public String getSettingName() {
		return settingName;
	}
}
