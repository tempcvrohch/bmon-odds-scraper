package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

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
	private Long id;

	@Column(nullable = false)
	private String marketName;

	@Column(nullable = false)
	private String playerName;

	@Column(name = "b365_fixture_id", nullable = false)
	private Long fixtureId;

	//this is the bet365 bet_id, no reference to models/db/Bet
	@Column(name = "b365_bet_id", nullable = false)
	private Long betId;

	@Column(nullable = false)
	private boolean suspended;

	@Column(nullable = false)
	private String odd;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "match_state_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private MatchState matchState;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Long getFixtureId() {
		return fixtureId;
	}

	public void setFixtureId(Long fixtureId) {
		this.fixtureId = fixtureId;
	}

	public Long getBetId() {
		return betId;
	}

	public void setBetId(Long betId) {
		this.betId = betId;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public String getOdd() {
		return odd;
	}

	public void setOdd(String odd) {
		this.odd = odd;
	}

	public MatchState getMatchState() {
		return matchState;
	}

	public void setMatchState(MatchState matchState) {
		this.matchState = matchState;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MarketState that = (MarketState) o;
		return Objects.equals(fixtureId, that.fixtureId) &&
				Objects.equals(betId, that.betId) &&
				Objects.equals(suspended, that.suspended) &&
				Objects.equals(odd, that.odd);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fixtureId, betId, suspended, odd);
	}
}
