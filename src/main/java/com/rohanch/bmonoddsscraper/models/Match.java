package com.rohanch.bmonoddsscraper.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Id;
import javax.persistence.Transient;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"id",
		"name",
		"sportName",
		"leagueName",
		"setScore",
		"matchState"
})

public class Match {

	@Id
	@JsonProperty("id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("sportName")
	private String sportName;
	@JsonProperty("leagueName")
	private String leagueName;

	@JsonInclude()
	@Transient
	@JsonProperty("matchState")
	private MatchState matchState = null;

	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("sportName")
	public String getSportName() {
		return sportName;
	}

	@JsonProperty("sportName")
	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	@JsonProperty("leagueName")
	public String getLeagueName() {
		return leagueName;
	}

	@JsonProperty("leagueName")
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	@JsonProperty("matchState")
	public MatchState getMatchState() {
		return matchState;
	}

	@JsonProperty("matchState")
	public void setMatchState(MatchState matchState) {
		this.matchState = matchState;
	}
}
