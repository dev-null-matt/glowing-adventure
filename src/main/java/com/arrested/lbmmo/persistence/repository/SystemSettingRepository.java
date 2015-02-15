package com.arrested.lbmmo.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arrested.lbmmo.persistence.entity.SystemSetting;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, String> {

	public Set<SystemSetting> findByName(String name);
}
