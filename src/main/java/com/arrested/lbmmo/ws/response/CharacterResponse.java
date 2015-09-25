package com.arrested.lbmmo.ws.response;

public class CharacterResponse {

	private String name;
	private String characterClass;
	private int level;
	
	public CharacterResponse() {
		characterClass = "";
		level = 1;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCharacterClass() {
		return characterClass;
	}

	public void setCharacterClass(String characterClass) {
		this.characterClass = characterClass;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
