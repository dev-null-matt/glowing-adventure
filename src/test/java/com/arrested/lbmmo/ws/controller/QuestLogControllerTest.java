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
import com.arrested.lbmmo.persistence.repository.QuestRepository;

public class QuestLogControllerTest extends AbstractMockedActiveUserServiceTest {
	
	@Mock
	private QuestRepository questRepo;
	
	@InjectMocks
	private QuestLogController controller;
	
	private List<Quest> quests;
	
	@Before
	public void init() {
		
		quests = new ArrayList<Quest>();
		
		for (int i = 0; i < 5; i++) {
			Quest quest = new Quest();
			quest.setId(i);
			quest.setName("Quest " + i);
			quest.setObjectives(new HashSet<Objective>());
			
			quests.add(quest);
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
		
		Assert.assertEquals("Proper number of quests with no quests in log", quests.size() - 1, controller.getAvailableQuests().size());
	}
}
