package com.arrested.lbmmo.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource("classpath:config/game-config.properties")
public class SystemSettingDao {
	
	private Map<SystemSettings, SystemSetting> settings;
	
	@Autowired
	private Environment env;
	
	@PostConstruct
	private void init() {
		
		settings = new HashMap<>();
		
		for (SystemSettings setting : SystemSettings.values()) {
			
			String value = env.getProperty(setting.getSettingName());
			SystemSetting systemSetting = new SystemSetting();
			
			if (setting.getType() == Boolean.class) {
				systemSetting.setEnabled(Boolean.parseBoolean(value));
			} else if (setting.getType() == Integer.class) {
				systemSetting.setIntValue(Integer.parseInt(value));
			} else {
				systemSetting.setStringValue(value);
			}
			
			settings.put(setting, systemSetting);
		}
	}
	
	public SystemSetting getSystemSetting(SystemSettings setting) {
		return settings.get(setting);
	}
}
