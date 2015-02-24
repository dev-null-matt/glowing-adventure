package com.arrested.lbmmo.ws.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrested.lbmmo.persistence.entity.Character;
import com.arrested.lbmmo.persistence.entity.Objective;
import com.arrested.lbmmo.persistence.entity.Quest;
import com.arrested.lbmmo.persistence.entity.QuestInProgress;
import com.arrested.lbmmo.persistence.repository.CharacterRepository;
import com.arrested.lbmmo.persistence.repository.QuestRepository;
import com.arrested.lbmmo.ws.bean.response.LocationBean;
import com.arrested.lbmmo.ws.bean.response.QuestBean;

@RestController
@RequestMapping("/service/quest-log/")
public class QuestLogController extends AbstractServiceController {
	
	@Autowired
	private CharacterRepository characterRepo;
	
	@Autowired
	private QuestRepository questRepo;
	
	@RequestMapping(value="quest-status", method=RequestMethod.GET)
	public QuestBean getQuestStatus() {
		
		Character character = getServiceUser().getLoggedInCharacter();
		QuestBean questBean = null;

		if (character != null) {
			QuestInProgress qip = character.getTrackedQuestInProgress();
			
			if (qip != null) {
				questBean = populateQuestBean(qip);
			}
		}
		
		return questBean;
	}
	
	@RequestMapping(value="inactive-quests", method=RequestMethod.GET)
	public Set<QuestBean> getInactiveQuests() {
		
		Character character = getServiceUser().getLoggedInCharacter();
		QuestInProgress trackedQuest = character.getTrackedQuestInProgress();
		Set<QuestBean> quests = new HashSet<QuestBean>();
		
		if (character != null) {
			for (QuestInProgress qip : character.getQuestsInProgress()) {
				if (trackedQuest != null && trackedQuest.getId() != qip.getId()) {
					quests.add(populateQuestBean(qip));
				}
			}
		}
		
		return quests;
	}
	
	@RequestMapping(value="available-quests", method=RequestMethod.GET)
	public Set<QuestBean> getAvailableQuests() {
		
		Character character = getServiceUser().getLoggedInCharacter();
		
		Set<QuestBean> quests = new HashSet<QuestBean>();
		Set<QuestInProgress> inProgress = character.getQuestsInProgress();
		
		for (Quest quest : questRepo.findAll()) {
			
			boolean isInProgress = false;
			
			for (QuestInProgress qip : inProgress) {
				isInProgress |= quest.equals(qip.getQuest());
 			}
			
			if (!isInProgress) {
				quests.add(populateQuestBean(quest));
			}
		}
		
		return quests;
	}
	
	@RequestMapping(value="accept-quest/{questId}", method=RequestMethod.POST) 
	public String acceptQuest(@PathVariable String questId) {
		
		Character character = getServiceUser().getLoggedInCharacter();
		boolean isDuplicate = false;
		Quest questToAdd = null;
		
		try {
			questToAdd = questRepo.findOne(Long.parseLong(questId));
		} catch (NumberFormatException e) {
			// TODO: Return an error message
		}
		
		for (QuestInProgress qip : character.getQuestsInProgress()) {
			isDuplicate |= qip.getQuest().equals(questToAdd);
		}
		
		if (isDuplicate) {
			// TODO: Return an error message
		} else {
			character.getQuestsInProgress().add(populateQuestInProgress(questToAdd, character));
			characterRepo.save(character);
		}
		
		return null;
	}
	
	private QuestBean populateQuestBean(QuestInProgress qip) {
		
		Objective objective = qip.getCurrentObjective();
		
		QuestBean questBean = new QuestBean();
		LocationBean locationBean = new LocationBean();
		
		questBean.setQuestName(objective.getQuest().getName());
		
		locationBean.setLatitude(objective.getWaypoint().getLatitude());
		locationBean.setLongitude(objective.getWaypoint().getLongitude());
		
		questBean.setNextObjective(locationBean);
		
		return questBean;
	}
	
	private QuestBean populateQuestBean(Quest quest) {
		
		QuestBean questBean = new QuestBean();
		LocationBean locationBean = new LocationBean();
		
		questBean.setQuestName(quest.getName());
		
		for (Objective objective : quest.getObjectives()) {
		
			if (objective.getQuestStep() == 0) {
				locationBean.setLatitude(objective.getWaypoint().getLatitude());
				locationBean.setLongitude(objective.getWaypoint().getLongitude());				
			}
		}
		
		questBean.setNextObjective(locationBean);
		
		return questBean;
	}
	
	private QuestInProgress populateQuestInProgress(Quest quest, Character character) {

		QuestInProgress qip = new QuestInProgress();
		
		return qip;
	}
}
