package com.arrested.lbmmo.ws.controller;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.SystemSetting;
import com.arrested.lbmmo.persistence.repository.CharacterRepository;
import com.arrested.lbmmo.persistence.repository.UserRepository;
import com.arrested.lbmmo.util.SystemSettingDao;
import com.arrested.lbmmo.util.SystemSettings;

public class CharacterCreationControllerTest extends AbstractMockedActiveUserServiceTest {
	
	@Mock
	private CharacterRepository characterRepo;
	
	@Mock
	private SystemSettingDao systemSettingsDao;
	
	@Mock
	private UserRepository userRepo;
	
	@InjectMocks
	private CharacterCreationController controller;
	
	private Set<Character> characters;
	
	@Before
	public void init() {
		characters = new HashSet<Character>();
		characters.add(new Character());
	}
	
	@Test
	public void canCreateCharacterNegativeTest() {
	
		SystemSetting setting = new SystemSetting();
		setting.setIntValue(1);

		Mockito.when(systemSettingsDao.getSystemSetting(SystemSettings.ACCOUNT_MAX_CHARACTERS)).thenReturn(setting);

		Assert.assertFalse(controller.canCreateCharacter());
	}
	
	@Test
	public void createCharacterNameExistsTest() {
		
		Mockito.when(characterRepo.findByNameIgnoreCaseAndUserId("testCharacter", 1)).thenReturn(characters);
		
		Assert.assertFalse(controller.createCharacter("testCharacter"));
	}
	
	@Test
	public void createCharacterUserAtMaxTest() {

		SystemSetting setting = new SystemSetting();
		setting.setIntValue(1);

		Mockito.when(systemSettingsDao.getSystemSetting(SystemSettings.ACCOUNT_MAX_CHARACTERS)).thenReturn(setting);

		Assert.assertFalse(controller.createCharacter("testCharacter"));
	}
}
