package com.arrested.lbmmo.ws.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.CharacterRepository;
import com.arrested.lbmmo.persistence.repository.UserRepository;
import com.arrested.lbmmo.ws.response.CharacterResponse;

@RestController
@RequestMapping("/service/main-menu/")
public class MainMenuController extends AbstractServiceController {
	
	@Autowired
	private CharacterRepository characterRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired(required=true)
	private HttpServletRequest request;
	
	@RequestMapping(value="characters", method=RequestMethod.GET)
	public List<CharacterResponse> characterList() {
		
		List<CharacterResponse> characterBeans = new ArrayList<CharacterResponse>();
		
		for (Character character : getServiceUser().getCharacters()) {
			
			CharacterResponse cb = new CharacterResponse();
			cb.setName(character.getName());
			
			characterBeans.add(cb);
		}
		
		return characterBeans;
	}
		
	@RequestMapping(value="login/{characterName}")
	@Transactional
	public void login(@PathVariable String characterName) {
		
		if (characterName.equals("-")) {
			return;
		}
		
		User user = getServiceUser();
		Character oldCharacter = user.getLoggedInCharacter();
		Character newCharacter = null;
		
		if (oldCharacter == null || !oldCharacter.getName().equalsIgnoreCase(characterName)) {
			
			newCharacter = user.getCharacterByName(characterName);
			
			if (oldCharacter != null) {
				oldCharacter.isLoggedIn(false);
				characterRepo.save(oldCharacter);
			}
			
			if (newCharacter != null) {
				newCharacter.isLoggedIn(true);
				characterRepo.save(newCharacter);
			}
		}
	}
	
	@RequestMapping(value="logout", method=RequestMethod.PUT)
	@Transactional
	public void logout() {
		
		Character character = getServiceUser().getLoggedInCharacter();
		
		if (character != null) {
			character.isLoggedIn(false);
			characterRepo.save(character);
		}
	}
	
	@RequestMapping(value="delete", method=RequestMethod.DELETE)
	@Transactional
	public void delete(@RequestParam("name") String characterName) {
		
		Character character = getServiceUser().getCharacterByName(characterName);
		
		characterRepo.delete(character);
	}
	
	@RequestMapping(value="exit", method=RequestMethod.PUT)
	public void exit() {
		request.getSession().invalidate();
	}
}
