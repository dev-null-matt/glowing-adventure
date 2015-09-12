package com.arrested.lbmmo.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuestInProgressTest {
	private QuestInProgress qip;
	private Quest quest;
	
	@Before
	public void init() {
		
		quest = new Quest();
		
		Objective objective1 = new Objective();
		objective1.setQuestStep(1);
		
		Objective objective2 = new Objective();
		objective2.setQuestStep(2);
		
		Set<Objective> objectives = new HashSet<>();
		objectives.add(objective1);
		objectives.add(objective2);
		
		quest.setObjectives(objectives);
		
		qip = new QuestInProgress();
		qip.setQuest(quest);
	}
	
	@Test
	public void isCompleteTest_onFirstObjective() {
		
		qip.setCurrentStep(1);
		
		Assert.assertEquals(false, qip.isComplete());
	}
	
	@Test
	public void isCompleteTest_onLastObjective() {
		
		qip.setCurrentStep(2);
		
		Assert.assertEquals(false, qip.isComplete());
	}
	
	@Test
	public void isCompleteTest_pastLastObjective() {

		qip.setCurrentStep(3);
		
		Assert.assertEquals(true, qip.isComplete());
	}
}
