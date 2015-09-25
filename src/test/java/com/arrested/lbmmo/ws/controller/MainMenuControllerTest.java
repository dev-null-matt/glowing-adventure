package com.arrested.lbmmo.ws.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.CharacterRepository;
import com.arrested.lbmmo.persistence.repository.UserRepository;
import com.arrested.lbmmo.ws.response.CharacterResponse;

public class MainMenuControllerTest extends AbstractMockedActiveUserServiceTest {

	@Mock
	private CharacterRepository characterRepo;
	
	@Mock
	private UserRepository userRepo;
	
	@Mock
	private HttpServletRequest request;
	
	@InjectMocks
	private MainMenuController controller;
	
	@Test
	public void characterListTest_smokeTest() {
		
		CharacterResponse[] characters = controller.characterList().toArray(new CharacterResponse[0]);
		
		Assert.assertEquals(1, characters.length);
		Assert.assertEquals("Character 1", characters[0].getName());
	}
	
	@Test
	public void loginTest_smokeTest() {
		
		Character character1 = activeUserService.getActiveUser().getCharacterByName("Character 1");
		
		controller.login(character1.getName());
		
		Mockito.verify(characterRepo).save(character1);
		Mockito.verify(characterRepo, Mockito.atMost(1)).save(Mockito.any(Character.class));
		
		Assert.assertEquals(true, character1.isLoggedIn());
	}
	
	@Test
	public void loginTest_loginAlreadyLoggedInCharacter() {
		
		Character character1 = activeUserService.getActiveUser().getCharacterByName("Character 1");
		character1.isLoggedIn(true);
		
		controller.login(character1.getName());
		
		Mockito.verify(characterRepo, Mockito.never()).save(Mockito.any(Character.class));
		
		Assert.assertEquals(true, character1.isLoggedIn());
	}
	
	@Test
	public void loginTest_anotherCharacterAlreadyLoggedIn() {

		User user = activeUserService.getActiveUser();
		
		Character character1 = user.getCharacterByName("Character 1");
		
		Character character2 = new Character("Character 2", user);
		character2.isLoggedIn(true);
		
		user.getCharacters().add(character2);
		
		controller.login(character1.getName());

		Mockito.verify(characterRepo).save(character1);
		Mockito.verify(characterRepo).save(character2);

		Assert.assertEquals(true, character1.isLoggedIn());
		Assert.assertEquals(false, character2.isLoggedIn());
	}
	
	@Test
	public void logoutTest_smokeTest() {
		
		Character character1 = activeUserService.getActiveUser().getCharacterByName("Character 1");
		character1.isLoggedIn(true);
		
		Mockito.when(request.getSession()).thenReturn(Mockito.mock(HttpSession.class));
		
		controller.logout();
		
		Mockito.verify(characterRepo).save(character1);
		Mockito.verify(characterRepo, Mockito.atMost(1)).save(Mockito.any(Character.class));
		
		Assert.assertEquals(false, character1.isLoggedIn());
	}
	
	@Test
	public void logoutTest_noLoggedInCharacter() {

		Mockito.when(request.getSession()).thenReturn(Mockito.mock(HttpSession.class));
		
		controller.logout();
		
		Mockito.verify(characterRepo, Mockito.never()).save(Mockito.any(Character.class));
	}
}
