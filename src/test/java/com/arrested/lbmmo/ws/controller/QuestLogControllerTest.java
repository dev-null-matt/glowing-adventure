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
	
	private static final String QUEST_NAME = "TEST_QUEST";
	
	@Mock
	private CharacterRepository characterRepo;
	
	@Mock
	private QuestRepository questRepo;
	
	@Mock
	private SystemSettingDao settingDao;
	
	@InjectMocks
	private QuestLogController controller;
	
	private List<Quest> quests;
	
	@Before
	public void init() {
		
		Quest quest = new Quest();
		quest.setName(QUEST_NAME);
		
		Mockito.when(questRepo.findOne(0l)).thenReturn(quest);
		
		quests = new ArrayList<Quest>();
		
		for (int i = 0; i < 5; i++) {
			Quest q = new Quest();
			q.setId(i);
			q.setName("Quest " + i);
			q.setObjectives(new HashSet<Objective>());
			
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
		
		Assert.assertEquals("Proper number of quests", quests.size() - 1, controller.getAvailableQuests().size());
	}
	
	@Test
	public void acceptQuestTest_smokeTest() {
		
		Character character = activeUserService.getActiveUser().getLoggedInCharacter();
		
		controller.acceptQuest("0");
		
		Assert.assertEquals(1, character.getQuestsInProgress().size());
		Assert.assertEquals(QUEST_NAME, character.getQuestsInProgress().iterator().next().getQuest().getName());
	}
	
	@Test
	public void acceptQuestTest_maxQuests() {
		
		Character character = activeUserService.getActiveUser().getLoggedInCharacter();
		
		SystemSetting setting = new SystemSetting();
		setting.setIntValue(1);
		
		Quest quest = new Quest();
		quest.setName("OTHER_QUEST");
		
		Mockito.when(questRepo.findOne(1l)).thenReturn(quest);
		
		Mockito.when(settingDao.getSystemSetting(SystemSettings.CHARACTER_MAX_QUESTS)).thenReturn(setting);
		
		controller.acceptQuest("0");
		controller.acceptQuest("1");
		
		Assert.assertEquals(1, character.getQuestsInProgress().size());
		Assert.assertEquals(QUEST_NAME, character.getQuestsInProgress().iterator().next().getQuest().getName());
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
		Assert.fail("Implementation incomplete");
	}
}
