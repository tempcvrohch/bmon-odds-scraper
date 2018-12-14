package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"id",
		"bId",
		"name",
		"sportName",
		"leagueName"
})

@Entity
@Table(name = "matches")
public class MatchEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("id")
	private Long id;

	@JsonProperty("bId")
	@Column(name = "b_id", unique = true, nullable = false)
	private String bId;

	@JsonProperty("name")
	@Column(name = "name", nullable = false)
	private String name;

	@JsonProperty("sportName")
	@Column(name = "sport_name", nullable = false)
	private String sportName;

	@JsonProperty("leagueName")
	@Column(name = "league_name", nullable = false)
	private String leagueName;

	@JsonInclude()
	@Transient
	@JsonProperty("matchState")
	private MatchStateEntity matchState = null;

	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	public String getbId() {
		return bId;
	}

	public void setbId(String bId) {
		this.bId = bId;
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
	public MatchStateEntity getMatchState() {
		return matchState;
	}

	@JsonProperty("matchState")
	public void setMatchState(MatchStateEntity matchState) {
		this.matchState = matchState;
	}
}
