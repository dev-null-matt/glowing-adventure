package com.arrested.lbmmo.ws.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
import com.arrested.lbmmo.persistence.entity.SystemSetting;
import com.arrested.lbmmo.persistence.entity.Waypoint;
import com.arrested.lbmmo.persistence.repository.QuestInProgressRepository;
import com.arrested.lbmmo.persistence.repository.QuestRepository;
import com.arrested.lbmmo.util.SystemSettingDao;
import com.arrested.lbmmo.util.SystemSettings;

public class QuestLogControllerTest extends AbstractMockedActiveUserServiceTest {
	
	private static final int QUESTS_IN_LOG = 5;
	
	@Mock
	private QuestRepository questRepo;
	
	@Mock
	private QuestInProgressRepository questInProgressRepo;
	
	@Mock
	private SystemSettingDao settingDao;
	
	@InjectMocks
	private QuestLogController controller;
	
	private Character character;
	
	@Before
	public void init() {
		
		List<Quest> quests = new ArrayList<Quest>();
		
		for (int i = 0; i < QUESTS_IN_LOG; i++) {
			
			Waypoint wp = new Waypoint();
			wp.setLatitude(0l);
			wp.setLongitude(0l);
			
			Objective objective = new Objective();
			objective.setWaypoint(wp);
			objective.setQuestStep(0);
			
			Quest q = new Quest();
			q.setId(i);
			q.setName("Quest " + i);
			q.setObjectives(new HashSet<Objective>());
			q.getObjectives().add(objective);
			
			Mockito.when(questRepo.findOne((long) i)).thenReturn(q);

			quests.add(q);			
		}
		
		Mockito.when(questRepo.findAll()).thenReturn(quests);
		
		activeUserService.getActiveUser().getCharacters().iterator().next().isLoggedIn(true);
		character = activeUserService.getActiveUser().getLoggedInCharacter();
	}
	
	@Test
	public void getInactiveQuestsTest() {
		
		addQuestToQuestsInProgress(character);
		
		Assert.assertEquals(1, controller.getInactiveQuests().size());
	}
	
	@Test
	public void getAvailableQuestsTest() {
		
		addQuestToQuestsInProgress(character);
		
		Assert.assertEquals("Proper number of quests", QUESTS_IN_LOG - 1, controller.getAvailableQuests().size());
	}
	
	private void addQuestToQuestsInProgress(Character character) {
		
		QuestInProgress qip = new QuestInProgress();
		qip.setQuest(questRepo.findOne(0l));
		qip.setCurrentStep(0);
		
		character.getQuestsInProgress().add(qip);
	}
	
	@Test
	public void acceptQuestTest_smokeTest() {

		controller.acceptQuest("0");
		
		Assert.assertEquals(1, character.getQuestsInProgress().size());
		Assert.assertEquals("Quest 0", character.getQuestsInProgress().iterator().next().getQuest().getName());
	}
	
	@Test
	public void acceptQuestTest_maxQuests() {

		SystemSetting setting = new SystemSetting();
		setting.setIntValue(1);
		
		Mockito.when(settingDao.getSystemSetting(SystemSettings.CHARACTER_MAX_QUESTS)).thenReturn(setting);
		
		controller.acceptQuest("0");
		controller.acceptQuest("1");
		
		Assert.assertEquals(1, character.getQuestsInProgress().size());
		Assert.assertEquals("Quest 0", character.getQuestsInProgress().iterator().next().getQuest().getName());
	}
	
	@Test
	public void acceptQuestTest_duplicateQuest() {

		controller.acceptQuest("0");
		
		character.getQuestsInProgress().iterator().next().setCurrentStep(1);
		
		controller.acceptQuest("0");
		
		Assert.assertEquals("Only has one quest", 1, character.getQuestsInProgress().size());
		Assert.assertEquals("Quest is first one added", 1, character.getQuestsInProgress().iterator().next().getCurrentStep());
	}
	
	@Test
	public void acceptQuestTest_OnlyAllowAvailableQuests() {
		
		Quest quest = new Quest();
		quest.setId(6);
		quest.setName("Not available");
		
		Mockito.when(questRepo.findOne(6l)).thenReturn(quest);
		
		Assert.assertEquals("This mission is not currently available to you", controller.acceptQuest("6"));
	}
}
