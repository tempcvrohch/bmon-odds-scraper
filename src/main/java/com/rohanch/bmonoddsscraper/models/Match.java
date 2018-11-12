
package com.rohanch.bmonoddsscraper.models;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"id",
		"name",
		"sportName",
		"leagueName",
		"setScore",
		"players"
})
public class Match {

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("sportName")
	private String sportName;
	@JsonProperty("leagueName")
	private String leagueName;
	@JsonProperty("setScore")
	private String setScore;
	@JsonProperty("players")
	private List<Player> players = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
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

	@JsonProperty("setScore")
	public String getSetScore() {
		return setScore;
	}

	@JsonProperty("setScore")
	public void setSetScore(String setScore) {
		this.setScore = setScore;
	}

	@JsonProperty("players")
	public List<Player> getPlayers() {
		return players;
	}

	@JsonProperty("players")
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
