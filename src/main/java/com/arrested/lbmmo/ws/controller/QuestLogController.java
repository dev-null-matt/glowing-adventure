package com.arrested.lbmmo.ws.controller;

import java.util.Date;
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
import com.arrested.lbmmo.persistence.repository.QuestInProgressRepository;
import com.arrested.lbmmo.persistence.repository.QuestRepository;
import com.arrested.lbmmo.util.SystemSetting;
import com.arrested.lbmmo.util.SystemSettingDao;
import com.arrested.lbmmo.util.SystemSettings;
import com.arrested.lbmmo.ws.response.LocationResponse;
import com.arrested.lbmmo.ws.response.QuestResponse;

@RestController
@RequestMapping("/service/quest-log/")
public class QuestLogController extends AbstractServiceController {

	@Autowired
	private QuestRepository questRepo;

	@Autowired
	private QuestInProgressRepository questInProgressRepo;
	
	@Autowired
	private SystemSettingDao settingDao;

	@RequestMapping(value = "quest-status", method = RequestMethod.GET)
	public QuestResponse getQuestStatus() {

		Character character = getServiceUser().getLoggedInCharacter();
		QuestResponse questBean = null;

		if (character != null) {
			questBean = populateQuestBean(character.getTrackedQuestInProgress());
		}

		return questBean;
	}

	@RequestMapping(value = "inactive-quests", method = RequestMethod.GET)
	public Set<QuestResponse> getInactiveQuests() {

		Character character = getServiceUser().getLoggedInCharacter();
		QuestInProgress trackedQuest = character.getTrackedQuestInProgress();
		Set<QuestResponse> quests = new HashSet<QuestResponse>();

		if (character != null) {
			for (QuestInProgress qip : questInProgressRepo.findIncompleteMissions(character.getId())) {
				if (trackedQuest == null || trackedQuest.getId() != qip.getId()) {
					quests.add(populateQuestBean(qip));
				}
			}
		}

		return quests;
	}
	
	@RequestMapping(value = "completed-quests", method = RequestMethod.GET)
	public Set<QuestResponse> getCompletedQuests() {
		
		Character character = getServiceUser().getLoggedInCharacter();
		Set<QuestResponse> quests = new HashSet<QuestResponse>();

		if (character != null) {
			for (QuestInProgress qip : questInProgressRepo.findCompleteMissions(character.getId())) {
				quests.add(populateQuestBean(qip));
			}
		}

		return quests;
	}

	@RequestMapping(value = "available-quests", method = RequestMethod.GET)
	public Set<QuestResponse> getAvailableQuests() {

		Set<Quest> quests = findAvailableQuests(getServiceUser().getLoggedInCharacter());
		Set<QuestResponse> questRepresentations = new HashSet<QuestResponse>();
		
		for (Quest quest : quests) {
			questRepresentations.add(populateQuestBean(quest));
		}

		return questRepresentations;
	}
	
	@RequestMapping(value = "accept-quest/{questId}", method = RequestMethod.POST)
	public String acceptQuest(@PathVariable String questId) {

		Character character = getServiceUser().getLoggedInCharacter();
		boolean isDuplicate = false;
		Quest questToAdd = null;

		try {
			questToAdd = questRepo.findOne(Long.parseLong(questId));
		} catch (NumberFormatException e) {
			return "No quests exists with the id " + questId;
		}

		for (QuestInProgress qip : character.getQuestsInProgress()) {
			isDuplicate |= qip.getQuest().equals(questToAdd);
		}

		if (isDuplicate) {
			return questToAdd.getName() + " is already in your mission log";
		}
		
		if (! findAvailableQuests(character).contains(questToAdd)) {
			return "This mission is not currently available to you";
		}
		
		SystemSetting maxQuests = settingDao.getSystemSetting(SystemSettings.CHARACTER_MAX_QUESTS);

		if (maxQuests != null && maxQuests.getIntValue() <= character.getQuestsInProgress().size()) {
			return "Your mission log is full";
		}

		QuestInProgress qip = populateQuestInProgress(questToAdd, character);
		character.getQuestsInProgress().add(qip);
		questInProgressRepo.save(qip);

		return questToAdd.getName() + " added to mission log";
	}

	@RequestMapping(value = "track-quest/{questId}", method = RequestMethod.PUT)
	public void trackQuest(@PathVariable String questId) {
		
		Character character = getServiceUser().getLoggedInCharacter();
		QuestInProgress newlyTrackedQip = null;
		long id = -1;
		
		try {
			id = Long.parseLong(questId);
		} catch (NumberFormatException e) {
			return;
		}
		
		for (QuestInProgress qip : character.getQuestsInProgress()) {
			if ( id == qip.getQuest().getId()) {
				newlyTrackedQip = qip;
			}
		}
		
		if (newlyTrackedQip != null) {
			
			QuestInProgress oldTrackedQip = character.getTrackedQuestInProgress();
			
			if (oldTrackedQip != null) {
				oldTrackedQip.isTracked(false);
				questInProgressRepo.save(oldTrackedQip);
			}
			
			newlyTrackedQip.isTracked(true);
			questInProgressRepo.save(newlyTrackedQip);
		}
	}
	
	private Set<Quest> findAvailableQuests(Character character) {

		Set<QuestInProgress> inProgress = character.getQuestsInProgress();
		Set<Quest> quests = new HashSet<Quest>();

		for (Quest quest : questRepo.findAll()) {

			boolean isInProgress = false;

			for (QuestInProgress qip : inProgress) {
				isInProgress |= quest.equals(qip.getQuest());
			}

			if (!isInProgress) {
				quests.add(quest);
			}
		}

		return quests;
	}
	
	private QuestResponse populateQuestBean(QuestInProgress qip) {

		QuestResponse questBean = new QuestResponse();
		
		if (qip == null) {
			return questBean;
		}
		
		Objective objective = qip.getCurrentObjective();
		
		LocationResponse locationBean = new LocationResponse();

		questBean.setQuestId(qip.getQuest().getId());
		questBean.setQuestName(qip.getQuest().getName());
		
		locationBean.setLatitude(objective.getWaypoint().getLatitude());
		locationBean.setLongitude(objective.getWaypoint().getLongitude());

		questBean.setNextObjective(locationBean);

		return questBean;
	}

	private QuestResponse populateQuestBean(Quest quest) {

		QuestResponse questBean = new QuestResponse();
		LocationResponse locationBean = new LocationResponse();

		questBean.setQuestId(quest.getId());
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
		qip.setQuest(quest);
		qip.setCurrentStep(0);
		qip.setCharacter(character);
		qip.setStartDate(new Date());

		return qip;
	}
}
