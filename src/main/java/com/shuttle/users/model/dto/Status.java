package com.shuttle.users.model.dto;

public enum Status {
	REST("휴식"),
	READY("준비"),
	GAME("게임");
	
	private String name;
	
	Status(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
