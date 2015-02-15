package com.arrested.lbmmo.util;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrested.lbmmo.persistence.entity.SystemSetting;
import com.arrested.lbmmo.persistence.repository.SystemSettingRepository;

@Component
public class SystemSettingDao {

	@Autowired
	private SystemSettingRepository systemSettingRepo;
	
	public SystemSetting getSystemSetting(SystemSettings setting) {
		
		Set<SystemSetting> settings = systemSettingRepo.findByName(setting.getSettingName());
		
		return settings.isEmpty() ? null : settings.iterator().next();
	}
}
