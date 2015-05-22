package com.arrested.lbmmo.ws.controller;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.Objective;
import com.arrested.lbmmo.persistence.entity.Quest;
import com.arrested.lbmmo.persistence.entity.QuestInProgress;
import com.arrested.lbmmo.persistence.repository.QuestInProgressRepository;
import com.arrested.lbmmo.service.DistanceCalculationService;
import com.arrested.lbmmo.ws.bean.request.PositionBean;
import com.arrested.lbmmo.ws.bean.response.EncounterBean;

public class MapControllerTest extends AbstractMockedActiveUserServiceTest {

	@Mock
	private DistanceCalculationService distanceService;
	
	@Mock
	private QuestInProgressRepository qipRepo;
	
	@InjectMocks
	private MapController controller;
	
	private Character character;
	
	@Before
	public void init() {
		activeUserService.getActiveUser().getCharacters().iterator().next().isLoggedIn(true);
		character = activeUserService.getActiveUser().getLoggedInCharacter();
	}
	
	@Test
	public void setNewPositionTest_noTrackedQip() {
		
		PositionBean position = new PositionBean(3.14, 3.14);
		
		EncounterBean encounter = controller.setNewPosition(position);
		
		Assert.assertTrue(encounter.getMessages().isEmpty());
		Assert.assertFalse(encounter.isCombatEncounter());
	}
	
	@Test
	public void setNewPositionTest_trackedQip_noObjectiveComplete() {
		
		activeUserService.getActiveUser().getLoggedInCharacter().getQuestsInProgress().add(generateQuestInProgress());
		
		PositionBean position = new PositionBean(3.14, 3.14);
		
		Mockito.when(distanceService.distanceToObjective(Mockito.any(PositionBean.class), Mockito.any(Objective.class))).thenReturn(100.0);
		
		EncounterBean encounter = controller.setNewPosition(position);
		
		Assert.assertTrue(encounter.getMessages().isEmpty());
		Assert.assertFalse(encounter.isCombatEncounter());
	}
	
	@Test
	public void setNewPositionTest_trackedQip_objectiveComplete() {
		
		activeUserService.getActiveUser().getLoggedInCharacter().getQuestsInProgress().add(generateQuestInProgress());
		
		PositionBean position = new PositionBean(3.14, 3.14);
		
		Mockito.when(distanceService.distanceToObjective(Mockito.any(PositionBean.class), Mockito.any(Objective.class))).thenReturn(5.0);
		
		EncounterBean encounter = controller.setNewPosition(position);
		
		Assert.assertEquals(1, encounter.getMessages().size());
		
		Assert.assertTrue(encounter.getMessages().contains("Updating TEST_QUEST"));
		Assert.assertFalse(encounter.isCombatEncounter());
	}
	
	public QuestInProgress generateQuestInProgress() {

		Quest quest = new Quest();
		quest.setName("TEST_QUEST");
		
		Objective objective = new Objective();
		objective.setQuest(quest);
		
		Set<Objective> objectives = new HashSet<Objective>();
		objectives.add(objective);
		
		QuestInProgress qip = new QuestInProgress();
		qip.setQuest(quest);
		qip.getQuest().setObjectives(objectives);
		qip.isTracked(true);
		
		return qip;
	}
}
