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
import com.arrested.lbmmo.persistence.repository.CharacterRepository;
import com.arrested.lbmmo.persistence.repository.QuestRepository;
import com.arrested.lbmmo.util.SystemSettingDao;
import com.arrested.lbmmo.util.SystemSettings;

public class QuestLogControllerTest extends AbstractMockedActiveUserServiceTest {
	
	private static final int QUESTS_IN_LOG = 5;
	
	@Mock
	private CharacterRepository characterRepo;
	
	@Mock
	private QuestRepository questRepo;
	
	@Mock
	private SystemSettingDao settingDao;
	
	@InjectMocks
	private QuestLogController controller;
	
	@Before
	public void init() {
		
		List<Quest> quests = new ArrayList<Quest>();
		
		for (int i = 0; i < QUESTS_IN_LOG; i++) {
			
			Quest q = new Quest();
			q.setId(i);
			q.setName("Quest " + i);
			q.setObjectives(new HashSet<Objective>());
			
			Mockito.when(questRepo.findOne((long) i)).thenReturn(q);

			quests.add(q);			
		}
		
		Mockito.when(questRepo.findAll()).thenReturn(quests);
		
		activeUserService.getActiveUser().getCharacters().iterator().next().isLoggedIn(true);
	}
	
	@Test
	public void getAvailableQuestsTest() {

		Character character = activeUserService.getActiveUser().getLoggedInCharacter();
		
		Quest quest = new Quest();
		quest.setId(0);
		quest.setName("Quest 0");
		quest.setObjectives(new HashSet<Objective>());
		
		QuestInProgress qip = new QuestInProgress();
		qip.setQuest(quest);
		qip.setCurrentStep(0);
		
		character.getQuestsInProgress().add(qip);
		
		Assert.assertEquals("Proper number of quests", QUESTS_IN_LOG - 1, controller.getAvailableQuests().size());
	}
	
	@Test
	public void acceptQuestTest_smokeTest() {
		
		Character character = activeUserService.getActiveUser().getLoggedInCharacter();
		
		controller.acceptQuest("0");
		
		Assert.assertEquals(1, character.getQuestsInProgress().size());
		Assert.assertEquals("Quest 0", character.getQuestsInProgress().iterator().next().getQuest().getName());
	}
	
	@Test
	public void acceptQuestTest_maxQuests() {
		
		Character character = activeUserService.getActiveUser().getLoggedInCharacter();
		
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
		
		Character character = activeUserService.getActiveUser().getLoggedInCharacter();
		
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
