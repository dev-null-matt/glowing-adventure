package com.arrested.lbmmo.ws.controller;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.QuestInProgress;
import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.service.ActiveUserService;

public abstract class AbstractMockedActiveUserServiceTest {

	@Mock
	protected ActiveUserService activeUserService;
	
	@Before
	public void initAbstractMockedActiveUserServiceTest() {
		
		MockitoAnnotations.initMocks(this);
		
		User user = new User();
		
		Character character = new Character("Character 1",user);
		character.setQuestsInProgress(new HashSet<QuestInProgress>());
		
		Set<Character> characters = new HashSet<Character>();
		characters.add(character);
		
		user.setId(1);
		user.setCharacters(characters);
		
		Mockito.when(activeUserService.getActiveUser()).thenReturn(user);
	}
}
