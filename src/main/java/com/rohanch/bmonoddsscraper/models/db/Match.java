package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonInclude;
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
	private Long id;

	@Column(unique = true, nullable = false)
	private String bId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String sportName;

	@Column(nullable = false)
	private String leagueName;

	@JsonInclude()
	@OneToOne()
	private MatchState matchState = null;

	@JsonInclude()
	@Transient
	private MatchState[] matchStates = null;

	@Transient
	@JsonInclude()
	private boolean live;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getbId() {
		return bId;
	}

	public void setbId(String bId) {
		this.bId = bId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public MatchState getMatchState() {
		return matchState;
	}

	public void setMatchState(MatchState matchState) {
		this.matchState = matchState;
	}

	public MatchState[] getMatchStates() {
		return matchStates;
	}

	public void setMatchStates(MatchState[] matchStates) {
		this.matchStates = matchStates;
	}

	public boolean isLive() {
		return live;
	}

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
