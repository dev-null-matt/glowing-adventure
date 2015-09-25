package com.arrested.lbmmo.ws.response;

import java.util.Set;

import com.arrested.lbmmo.persistence.entity.Quest;
import com.arrested.lbmmo.persistence.entity.QuestInProgress;

public class QuestLogResponse {

	private QuestInProgress activeQuest;
	
	private Set<QuestInProgress> questsInProgress;
	
	private Set<Quest> availableQuests;

	public QuestInProgress getActiveQuest() {
		return activeQuest;
	}

	public void setActiveQuest(QuestInProgress activeQuest) {
		this.activeQuest = activeQuest;
	}

	public Set<QuestInProgress> getQuestsInProgress() {
		return questsInProgress;
	}

	public void setQuestsInProgress(Set<QuestInProgress> questsInProgress) {
		this.questsInProgress = questsInProgress;
	}

	public Set<Quest> getAvailableQuests() {
		return availableQuests;
	}

	public void setAvailableQuests(Set<Quest> availableQuests) {
		this.availableQuests = availableQuests;
	}
}
