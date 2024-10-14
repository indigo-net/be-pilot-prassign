package com.shuttle.users.model.dto;


public class User {
	private String uuid, userName, arriveTimeStamp, status, fcmToken;

	public User(){}
	
	public User(String uuid, String userName, String arriveTimeStamp, String status, String fcmToken) {
		super();
		this.uuid = uuid;
		this.userName = userName;
		this.arriveTimeStamp = arriveTimeStamp;
		this.status = status;
		this.fcmToken = fcmToken;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getArriveTimeStamp() {
		return arriveTimeStamp;
	}

	public void setArriveTimeStamp(String arriveTimeStamp) {
		this.arriveTimeStamp = arriveTimeStamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}
	
	
	
}
