package com.arrested.lbmmo.util;

public enum SystemSettings {

	ACCOUNT_MAX_CHARACTERS("ACCOUNT.MAX_CHARACTERS", Integer.class),
	CHARACTER_MAX_QUESTS("CHARACER.MAX_QUESTS", Boolean.class);
	
	private String settingName;
	private Class<? extends Object> type;
	
	private SystemSettings(String settingName, Class<? extends Object> type) {
		this.settingName = settingName;
		this.type = type;
	}
	
	public String getSettingName() {
		return settingName;
	}
	
	public Class<? extends Object> getType() {
		return type;
	}
}
