package com.shuttle.users.model.dto;

public enum Status {
	WAIT("대기"),
	READY("준비"),
	RUN("진행");
	
	private String name;
	
	Status(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
