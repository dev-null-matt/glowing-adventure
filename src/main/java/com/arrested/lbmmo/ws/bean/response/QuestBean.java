package com.arrested.lbmmo.ws.bean.response;

public class QuestBean {

	private LocationBean nextObjective;
	private String questName;

	public LocationBean getNextObjective() {
		return nextObjective;
	}
	
	public void setNextObjective(LocationBean nextObjective) {
		this.nextObjective = nextObjective;
	}
	
	public String getQuestName() {
		return questName;
	}
	
	public void setQuestName(String questName) {
		this.questName = questName;
	}
}
