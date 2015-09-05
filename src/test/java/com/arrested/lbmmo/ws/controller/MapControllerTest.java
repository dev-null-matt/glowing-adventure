package com.arrested.lbmmo.ws.controller;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.arrested.lbmmo.persistence.enitity.roles.UserRoleType;
import com.arrested.lbmmo.persistence.entity.Objective;
import com.arrested.lbmmo.persistence.entity.Quest;
import com.arrested.lbmmo.persistence.entity.QuestInProgress;
import com.arrested.lbmmo.persistence.entity.UserRole;
import com.arrested.lbmmo.persistence.entity.Waypoint;
import com.arrested.lbmmo.persistence.repository.QuestInProgressRepository;
import com.arrested.lbmmo.persistence.repository.WaypointRepository;
import com.arrested.lbmmo.service.DistanceCalculationService;
import com.arrested.lbmmo.ws.bean.response.EncounterBean;

public class MapControllerTest extends AbstractMockedActiveUserServiceTest {

	@Mock
	private DistanceCalculationService distanceService;
	
	@Mock
	private QuestInProgressRepository qipRepo;
	
	@Mock
	private WaypointRepository waypointRepo;
	
	@InjectMocks
	private MapController controller;
	
	@Before
	public void init() {
		activeUserService.getActiveUser().getCharacters().iterator().next().isLoggedIn(true);
	}
	
	@Test
	public void setNewPositionTest_noTrackedQip() {
		
		Waypoint position = new Waypoint();
		position.setLatitude(3.14);
		position.setLongitude(3.14);
		
		EncounterBean encounter = controller.setNewPosition(position);
		
		Assert.assertTrue(encounter.getMessages().isEmpty());
		Assert.assertFalse(encounter.isCombatEncounter());
	}
	
	@Test
	public void setNewPositionTest_trackedQip_noObjectiveComplete() {
		
		activeUserService.getActiveUser().getLoggedInCharacter().getQuestsInProgress().add(generateQuestInProgress(false));
		
		Waypoint position = new Waypoint();
		position.setLatitude(3.14);
		position.setLongitude(3.14);
		
		Mockito.when(distanceService.distanceToObjective(Mockito.any(Waypoint.class), Mockito.any(Objective.class))).thenReturn(100.0);
		
		EncounterBean encounter = controller.setNewPosition(position);
		
		Assert.assertTrue(encounter.getMessages().isEmpty());
		Assert.assertFalse(encounter.isCombatEncounter());
		Assert.assertFalse(encounter.isTrackedObjectiveUpdated());
	}
	
	@Test
	public void setNewPositionTest_untrackedQip_objectiveComplete() {
		
		activeUserService.getActiveUser().getLoggedInCharacter().getQuestsInProgress().add(generateQuestInProgress(false));
		
		Waypoint position = new Waypoint();
		position.setLatitude(3.14);
		position.setLongitude(3.14);
		
		Mockito.when(distanceService.distanceToObjective(Mockito.any(Waypoint.class), Mockito.any(Objective.class))).thenReturn(5.0);
		
		EncounterBean encounter = controller.setNewPosition(position);
		
		Assert.assertEquals(1, encounter.getMessages().size());
		Assert.assertTrue(encounter.getMessages().contains("Updating TEST_QUEST"));
		Assert.assertFalse(encounter.isCombatEncounter());
		Assert.assertFalse(encounter.isTrackedObjectiveUpdated());
	}
	
	@Test
	public void setNewPositionTest_trackedQip_objectiveComplete() {
		
		activeUserService.getActiveUser().getLoggedInCharacter().getQuestsInProgress().add(generateQuestInProgress(true));
		
		Waypoint position = new Waypoint();
		position.setLatitude(3.14);
		position.setLongitude(3.14);
		
		Mockito.when(distanceService.distanceToObjective(Mockito.any(Waypoint.class), Mockito.any(Objective.class))).thenReturn(5.0);
		
		EncounterBean encounter = controller.setNewPosition(position);
		
		Assert.assertEquals(1, encounter.getMessages().size());
		Assert.assertTrue(encounter.getMessages().contains("Updating TEST_QUEST"));
		Assert.assertFalse(encounter.isCombatEncounter());
		Assert.assertTrue(encounter.isTrackedObjectiveUpdated());
	}
	
	@Test
	public void createWaypoint_unverified() {
		
		Waypoint position = new Waypoint();
		position.setLatitude(3.14);
		position.setLongitude(3.14);
		position.setDescription("House of Pi");
		
		activeUserService.getActiveUser().setRoles(new HashSet<UserRole>());
		
		String message = controller.createWaypoint(position);
		
		Mockito.verifyZeroInteractions(waypointRepo);
		
		Assert.assertEquals("Only verified users can create new waypoints.", message);
	}
	
	@Test
	public void createWaypoint_verified() {
		
		Waypoint position = new Waypoint();
		position.setLatitude(3.14);
		position.setLongitude(3.14);
		position.setDescription("House of Pi");
		
		activeUserService.getActiveUser().setRoles(new HashSet<UserRole>());
		activeUserService.getActiveUser().assignRole(UserRoleType.VERIFIED);
		
		String message = controller.createWaypoint(position);
		
		Mockito.verify(waypointRepo).save(Mockito.any(Waypoint.class));
		
		Assert.assertEquals("Created new waypoint: House of Pi.", message);
	}
	
	public QuestInProgress generateQuestInProgress(boolean tracked) {

		Quest quest = new Quest();
		quest.setName("TEST_QUEST");
		
		Objective objective = new Objective();
		objective.setQuest(quest);
		
		Set<Objective> objectives = new HashSet<Objective>();
		objectives.add(objective);
		
		QuestInProgress qip = new QuestInProgress();
		qip.setQuest(quest);
		qip.getQuest().setObjectives(objectives);
		qip.isTracked(tracked);
		
		return qip;
	}
}
