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
public class MarketStateEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("id")
	private Long id;

	@JsonProperty("marketName")
	@Column(name = "market_name", nullable = false)
	private String marketName;

	@JsonProperty("playerName")
	@Column(name = "player_name", nullable = false)
	private String playerName;

	@JsonProperty("fixtureId")
	@Column(name = "fixture_id", nullable = false)
	private Long fixtureId;

	@JsonProperty("betId")
	@Column(name = "bet_id", nullable = false)
	private Long betId;

	@JsonProperty("suspended")
	@Column(name = "suspended", nullable = false)
	private String suspended;

	@JsonProperty("odd")
	@Column(name = "odd", nullable = false)
	private String odd;

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "match_state_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private MatchStateEntity matchState;

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

	public MatchStateEntity getMatchState() {
		return matchState;
	}

	public void setMatchState(MatchStateEntity matchState) {
		this.matchState = matchState;
	}
}
