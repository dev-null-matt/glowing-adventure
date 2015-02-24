package com.arrested.lbmmo.util;

public enum SystemSettings {

	ACCOUNT_MAX_CHARACTERS("ACCOUNT.MAX_CHARACTERS"),
	CHARACTER_MAX_QUESTS("CHARACER.MAX_QUESTS");
	
	
	private String settingName;
	
	private SystemSettings(String settingName) {
		this.settingName = settingName;
	}
	
	public String getSettingName() {
		return settingName;
	}
}
