package com.arrested.lbmmo.ws.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.enitity.roles.UserRoleType;
import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.Objective;
import com.arrested.lbmmo.persistence.entity.QuestInProgress;
import com.arrested.lbmmo.persistence.entity.Waypoint;
import com.arrested.lbmmo.persistence.repository.QuestInProgressRepository;
import com.arrested.lbmmo.persistence.repository.WaypointRepository;
import com.arrested.lbmmo.service.DistanceCalculationService;
import com.arrested.lbmmo.ws.response.EncounterResponse;

@RestController
@RequestMapping("/service/map/")
public class MapController extends AbstractServiceController {

	@Autowired
	private DistanceCalculationService distanceService;
	
	@Autowired
	private QuestInProgressRepository qipRepo;
	
	@Autowired
	private WaypointRepository waypointRepo;
	
	private static final int WAYPOINT_RADIUS = 20;
	
	@RequestMapping(value="create-waypoint", method=RequestMethod.POST, consumes = "application/json")
	@Transactional	
	public String createWaypoint(@RequestBody Waypoint waypoint) {
		
		String message = "Only verified users can create new waypoints.";
		
		if (getServiceUser().hasRole(UserRoleType.VERIFIED)) {
			if (StringUtils.isEmpty(waypoint.getDescription())) {
				waypoint.setDescription("a waypoint");
			}
			
			waypointRepo.save(waypoint);
			
			message = String.format("Created new waypoint: %s.", waypoint.getDescription());
		}
		
		return message;
	}
	
	@RequestMapping(value="set-new-position", method=RequestMethod.PUT, consumes = "application/json")
	public EncounterResponse setNewPosition(@RequestBody Waypoint position) {
		
		EncounterResponse encounter = new EncounterResponse();
		Character character = getServiceUser().getLoggedInCharacter();
		
		if (character == null) {
			return encounter;
		}
		
		for (QuestInProgress qip : character.getQuestsInProgress()) {
			Objective objective = qip.getCurrentObjective();
			if (objective != null && distanceService.distanceToObjective(position,objective) < WAYPOINT_RADIUS) {
				
				if (qip.isTracked()) {
					encounter.setTrackedObjectiveUpdated(true);
				}
				
				qip.setCurrentStep(qip.getCurrentStep()+1);
				
				if(qip.isComplete()) {
					encounter.getMessages().add(objective.getQuest().getName() + " complete");
					qip.setCompletedDate(new Date());
					qip.isTracked(false);
				} else {
					encounter.getMessages().add("Updating " + objective.getQuest().getName());
				}
				
				qipRepo.save(qip);
			}
		}
		
		if (character.getTrackedQuestInProgress() != null) {
			encounter.setMetersToNextObjective(distanceService.distanceToObjective(position, character.getTrackedQuestInProgress().getCurrentObjective()));
		}
		
		encounter.setCombatEncounter(false);

		return encounter;
	}
}
