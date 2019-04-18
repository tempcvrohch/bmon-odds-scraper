package com.rohanch.bmonoddsscraper.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"username",
		"balance",
		"pendingBetsAmount",
})

public class Session {
	@JsonProperty("username")
	private String username;

	@JsonProperty("balance")
	private Float balance;

	@JsonProperty("pendingBetsAmount")
	private Long pendingBetsAmount;

	public Session(String username, Float balance, Long pendingBetsAmount) {
		this.username = username;
		this.balance = balance;
		this.pendingBetsAmount = pendingBetsAmount;
	}

	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	@JsonProperty("username")
	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty("balance")
	public Float getBalance() {
		return balance;
	}

	@JsonProperty("balance")
	public void setBalance(Float balance) {
		this.balance = balance;
	}

	@JsonProperty("pendingBetsAmount")
	public Long getPendingBetsAmount() {
		return pendingBetsAmount;
	}

	@JsonProperty("pendingBetsAmount")
	public void setPendingBetsAmount(Long pendingBetsAmount) {
		this.pendingBetsAmount = pendingBetsAmount;
	}
}
