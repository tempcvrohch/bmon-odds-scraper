package com.rohanch.bmonoddsscraper.models.response;

import lombok.Data;

@Data
public class Session {
	private String username;
	private Float balance;
	private Long pendingBetsAmount;

	//TODO lombok builder
	public Session(String username, Float balance, Long pendingBetsAmount) {
		this.username = username;
		this.balance = balance;
		this.pendingBetsAmount = pendingBetsAmount;
	}
}
