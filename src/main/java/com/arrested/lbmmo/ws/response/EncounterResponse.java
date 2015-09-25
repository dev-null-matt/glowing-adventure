package com.arrested.lbmmo.ws.response;

import java.util.ArrayList;
import java.util.List;

public class EncounterResponse {

	private List<String> messages;
	
	private double metersToNextObjective;
	
	private boolean combatEncounter;

	private boolean trackedObjectiveUpdated;
	
	public EncounterResponse() {
		messages = new ArrayList<String>();
		combatEncounter = false;
		trackedObjectiveUpdated = false;
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
	
	public boolean isTrackedObjectiveUpdated() {
		return trackedObjectiveUpdated;
	}
	
	public void setTrackedObjectiveUpdated(boolean objectiveUpdated) {
		this.trackedObjectiveUpdated = objectiveUpdated;
	}
	
	public double getMetersToNextObjective() {
		return metersToNextObjective;
	}
	
	public void setMetersToNextObjective(double metersToNextObjective) {
		this.metersToNextObjective = metersToNextObjective;
	}
}
