package com.arrested.lbmmo.ws.controller;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.SystemSetting;
import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.CharacterRepository;
import com.arrested.lbmmo.persistence.repository.UserRepository;
import com.arrested.lbmmo.util.ActiveUserService;
import com.arrested.lbmmo.util.SystemSettingDao;
import com.arrested.lbmmo.util.SystemSettings;

public class CharacterCreationControllerTest {

	@Mock
	private ActiveUserService activeUserService;
	
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
		
		MockitoAnnotations.initMocks(this);
		
		characters = new HashSet<Character>();
		characters.add(new Character());
		
		User user = new User() {
			@Override
			public long getId() { 
				return 1;
			}
			
			@Override
			public Set<Character> getCharacters() {
				Set<Character> characters = new HashSet<Character>();
				characters.add(new Character());
				
				return characters;
			}
		};

		Mockito.when(activeUserService.getActiveUser()).thenReturn(user);
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
		
		Mockito.when(characterRepo.findByNameAndUserId("testCharacter", 1)).thenReturn(characters);
		
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
