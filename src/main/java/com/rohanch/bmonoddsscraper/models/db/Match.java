package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.util.Objects;

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
public class Match extends BaseEntity {
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
	@OneToOne() //, mappedBy = "match"
	@JsonProperty("matchState")
	private MatchState matchState = null;

	@JsonInclude()
	@Transient
	@JsonProperty("matchStates")
	private MatchState[] matchStates = null;

	@JsonProperty("live")
	@Transient
	@JsonInclude()
	private boolean live;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty("bId")
	public String getbId() {
		return bId;
	}

	@JsonProperty("bId")
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
	public MatchState getMatchState() {
		return matchState;
	}

	@JsonProperty("matchState")
	public void setMatchState(MatchState matchState) {
		this.matchState = matchState;
	}

	@JsonProperty("matchStates")
	public MatchState[] getMatchStates() {
		return matchStates;
	}

	@JsonProperty("matchStates")
	public void setMatchStates(MatchState[] matchStates) {
		this.matchStates = matchStates;
	}

	@JsonProperty("live")
	public boolean isLive() {
		return live;
	}

	@JsonProperty("live")
	public void setLive(boolean live) {
		this.live = live;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Match that = (Match) o;
		return Objects.equals(bId, that.bId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bId);
	}
}
