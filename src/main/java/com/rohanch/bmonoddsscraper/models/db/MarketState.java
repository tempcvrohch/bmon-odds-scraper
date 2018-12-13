package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"marketName",
		"playerName",
		"fixtureId",
		"betId",
		"suspended",
		"odd"
})

@Entity
@Table(name = "market_states")
public class MarketState extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("id")
	private Long id;

	@JsonProperty("marketName")
	private String marketName;
	@JsonProperty("playerName")
	private String playerName;
	@JsonProperty("fixtureId")
	private Long fixtureId;
	@JsonProperty("betId")
	private Long betId;
	@JsonProperty("suspended")
	private String suspended;
	@JsonProperty("odd")
	private String odd;

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "match_state_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private MatchState matchState;

	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty("marketName")
	public String getMarketName() {
		return marketName;
	}

	@JsonProperty("marketName")
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	@JsonProperty("playerName")
	public String getPlayerName() {
		return playerName;
	}

	@JsonProperty("playerName")
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@JsonProperty("fixtureId")
	public Long getFixtureId() {
		return fixtureId;
	}

	@JsonProperty("fixtureId")
	public void setFixtureId(Long fixtureId) {
		this.fixtureId = fixtureId;
	}

	@JsonProperty("betId")
	public Long getBetId() {
		return betId;
	}

	@JsonProperty("betId")
	public void setBetId(Long betId) {
		this.betId = betId;
	}

	@JsonProperty("suspended")
	public String getSuspended() {
		return suspended;
	}

	@JsonProperty("suspended")
	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}

	@JsonProperty("odd")
	public String getOdd() {
		return odd;
	}

	@JsonProperty("odd")
	public void setOdd(String odd) {
		this.odd = odd;
	}

	public MatchState getMatchState() {
		return matchState;
	}

	public void setMatchState(MatchState matchState) {
		this.matchState = matchState;
	}
}
