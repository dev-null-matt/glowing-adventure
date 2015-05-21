package com.arrested.lbmmo.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.Objective;
import com.arrested.lbmmo.persistence.entity.QuestInProgress;
import com.arrested.lbmmo.persistence.repository.QuestInProgressRepository;
import com.arrested.lbmmo.service.DistanceCalculationService;
import com.arrested.lbmmo.util.DistanceUtils;
import com.arrested.lbmmo.ws.bean.request.PositionBean;
import com.arrested.lbmmo.ws.bean.response.EncounterBean;

@RestController
@RequestMapping("/service/map/")
public class MapController extends AbstractServiceController {

	@Autowired
	private DistanceCalculationService distanceService;
	
	@Autowired
	private QuestInProgressRepository qipRepo;
	
	private static final int WAYPOINT_RADIUS = 20;
	
	@RequestMapping(value="set-new-position", method=RequestMethod.PUT, consumes = "application/json")
	public EncounterBean setNewPosition(@RequestBody PositionBean position) {
		
		EncounterBean encounter = new EncounterBean();
		Character character = getServiceUser().getLoggedInCharacter();
		
		if (character == null || character.getTrackedQuestInProgress() == null) {
			return encounter;
		}
		
		double distance = distanceService.distanceToObjective(position, character.getTrackedQuestInProgress().getCurrentObjective());
		
		for (QuestInProgress qip : character.getQuestsInProgress()) {
			Objective objective = qip.getCurrentObjective();
			if (objective != null && distanceService.distanceToObjective(position,objective) < WAYPOINT_RADIUS) {
				encounter.getMessages().add("Updating " + objective.getQuest().getName());
				qip.setCurrentStep(qip.getCurrentStep()+1);
				qipRepo.save(qip);
			}
		}
		
		encounter.setMetersToNextObjective(distance);
		encounter.setCombatEncounter(false);

		return encounter;
	}
}
