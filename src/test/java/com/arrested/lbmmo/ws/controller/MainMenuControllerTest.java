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

public class MainMenuControllerTest {

	@Mock
	private ActiveUserService activeUserService;
	
	@Mock
	private CharacterRepository characterRepo;
	
	@Mock
	private SystemSettingDao systemSettingsDao;
	
	@Mock
	private UserRepository userRepo;
	
	@InjectMocks
	private MainMenuController controller;
	
	private Set<Character> characters;
	
	@Before
	public void init() {
		
		characters = new HashSet<Character>();
		characters.add(new Character());
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void createCharacterNameExistsTest() {
		
		Mockito.when(characterRepo.findByName("testCharacter")).thenReturn(characters);
		
		Assert.assertFalse(controller.createCharacter("testCharacter"));
	}
	
	@Test
	public void createCharacterUserAtMaxTest() {

		SystemSetting setting = new SystemSetting();
		setting.setIntValue(1);
		
		User user = new User() {
			@Override
			public Set<Character> getCharacters() {
				Set<Character> characters = new HashSet<Character>();
				characters.add(new Character());
				
				return characters;
			}
		};
		
		Mockito.when(characterRepo.findByName("testCharacter2")).thenReturn(characters);
		Mockito.when(systemSettingsDao.getSystemSetting(SystemSettings.ACCOUNT_MAX_CHARACTERS)).thenReturn(setting);
		Mockito.when(activeUserService.getActiveUser()).thenReturn(user);
		
		Assert.assertFalse(controller.createCharacter("testCharacter"));
	}
}
