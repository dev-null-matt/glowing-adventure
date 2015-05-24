package com.arrested.lbmmo.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.SystemSetting;
import com.arrested.lbmmo.persistence.entity.User;
import com.arrested.lbmmo.persistence.repository.CharacterRepository;
import com.arrested.lbmmo.util.SystemSettingDao;
import com.arrested.lbmmo.util.SystemSettings;

@RestController
@RequestMapping(value="/service/character-creation/")
public class CharacterCreationController extends AbstractServiceController {

	@Autowired
	private CharacterRepository characterRepo;
	
	@Autowired
	private SystemSettingDao systemSettingsDao;
	
	@RequestMapping(value="can-create-character", method=RequestMethod.GET)
	public boolean canCreateCharacter() {
		
		SystemSetting maxCharacters = systemSettingsDao.getSystemSetting(SystemSettings.ACCOUNT_MAX_CHARACTERS);
		
		return getServiceUser().getCharacters().size() < maxCharacters.getIntValue();
	}
	
	@RequestMapping(value="isNameAvailable/{name}", method=RequestMethod.GET)
	public boolean isNameAvailable(@PathVariable String name) {
		return characterRepo.findByNameIgnoreCaseAndUserId(name, getServiceUser().getId()).isEmpty();
	}
	
	@RequestMapping(value="create-character/{characterName}", method=RequestMethod.PUT)
	@Transactional
	public boolean createCharacter(@PathVariable String characterName) {
		
		User user = getServiceUser();
		
		if (!isNameAvailable(characterName)) {
			return false;
		}
		
		if (!canCreateCharacter()) {
			return false;
		}
	
		characterRepo.save(new Character(characterName, user));
		
		return true;
	}
}
