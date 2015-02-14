package com.arrested.lbmmo.ws.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.CharacterRepository;
import com.arrested.lbmmo.persistence.repository.UserRepository;
import com.arrested.lbmmo.ws.bean.response.CharacterBean;

@RestController
@RequestMapping("/service/main-menu/")
public class MainMenuController extends AbstractServiceController {
	
	@Autowired
	private CharacterRepository characterRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired(required=true)
	private HttpServletRequest request;
	
	@RequestMapping("characters")
	public List<CharacterBean> characterList() {
		
		List<CharacterBean> characterBeans = new ArrayList<CharacterBean>();
		
		for (Character character : this.getServiceUser().getCharacters()) {
			
			CharacterBean cb = new CharacterBean();
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
		
		User user = userRepo.findByUsername(getServiceUser().getUsername()).get(0);
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
	
	@RequestMapping("logout")
	@Transactional
	public void logout() {
		
		Character character = userRepo.findByUsername(getServiceUser().getUsername()).get(0).getLoggedInCharacter();
		
		if (character != null && character.isLoggedIn()) {
			character.isLoggedIn(false);
			characterRepo.save(character);
		}
		
		request.getSession().invalidate();
	}
}
