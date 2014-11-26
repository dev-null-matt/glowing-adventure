package com.arrested.lbmmo.ws.bean.response;

import java.util.ArrayList;
import java.util.List;

public class EncounterBean {

	private List<String> messages;
	
	private boolean combatEncounter;

	public EncounterBean() {
		messages = new ArrayList<String>();
		combatEncounter = false;
	}
	
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public boolean isCombatEncounter() {
		return combatEncounter;
	}

	public void setCombatEncounter(boolean combatEncounter) {
		this.combatEncounter = combatEncounter;
	}
}
