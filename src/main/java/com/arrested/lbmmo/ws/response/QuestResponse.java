package com.arrested.lbmmo.ws.response;

public class QuestResponse {

	private LocationResponse nextObjective;
	private String questName;
	private Long questId;

	public LocationResponse getNextObjective() {
		return nextObjective;
	}
	
	public void setNextObjective(LocationResponse nextObjective) {
		this.nextObjective = nextObjective;
	}
	
	public String getQuestName() {
		return questName;
	}
	
	public void setQuestName(String questName) {
		this.questName = questName;
	}
	
	public Long getQuestId() {
		return questId;
	}
	
	public void setQuestId(long questId) {
		this.questId = questId;
	}
}
