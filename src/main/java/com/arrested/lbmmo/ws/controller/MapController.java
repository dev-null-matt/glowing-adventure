package com.arrested.lbmmo.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.arrested.lbmmo.ws.bean.request.PositionBean;
import com.arrested.lbmmo.ws.bean.response.EncounterBean;

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
	public String createWaypoint(@RequestBody PositionBean position) {
		
		String message = "Only verified users can create new waypoints.";
		
		if (getServiceUser().hasRole(UserRoleType.VERIFIED)) {
			Waypoint waypoint = new Waypoint();
			waypoint.setLatitude(position.getLatitude());
			waypoint.setLongitude(position.getLongitude());
			waypoint.setDescription(position.getDescription());
			
			waypointRepo.save(waypoint);
			
			message = "Created new waypoint: " + position.getDescription();
		}
		
		return message;
	}
	
	@RequestMapping(value="set-new-position", method=RequestMethod.PUT, consumes = "application/json")
	public EncounterBean setNewPosition(@RequestBody PositionBean position) {
		
		EncounterBean encounter = new EncounterBean();
		Character character = getServiceUser().getLoggedInCharacter();
		
		if (character == null) {
			return encounter;
		}
		
		for (QuestInProgress qip : character.getQuestsInProgress()) {
			Objective objective = qip.getCurrentObjective();
			if (objective != null && distanceService.distanceToObjective(position,objective) < WAYPOINT_RADIUS) {
				encounter.getMessages().add("Updating " + objective.getQuest().getName());
				qip.setCurrentStep(qip.getCurrentStep()+1);
				qipRepo.save(qip);
				
				if (qip.isTracked()) {
					encounter.setTrackedObjectiveUpdated(true);
				}
			}
		}
		
		if (character.getTrackedQuestInProgress() != null) {
			encounter.setMetersToNextObjective(distanceService.distanceToObjective(position, character.getTrackedQuestInProgress().getCurrentObjective()));
		}
		
		encounter.setCombatEncounter(false);

		return encounter;
	}
}
