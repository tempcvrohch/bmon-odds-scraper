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
		"id",
		"stake",
		"toReturn",
		"processed"
})

@Entity
@Table(name = "bets")
public class Bet extends BaseEntity {
	@JsonProperty("status")
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private BetStatus status;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("id")
	private Long id;

	@JsonProperty("stake")
	@Column(name = "stake", nullable = false)
	private Float stake;

	@JsonProperty("toReturn")
	@Column(name = "to_return", nullable = false)
	private Float toReturn;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "market_state_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private MarketState marketState;

	public BetStatus getStatus() {
		return status;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getStake() {
		return stake;
	}

	public void setStake(Float stake) {
		this.stake = stake;
	}

	public Float getToReturn() {
		return toReturn;
	}

	public void setToReturn(Float toReturn) {
		this.toReturn = toReturn;
	}

	public void setStatus(BetStatus status) {
		this.status = status;
	}

	public enum BetStatus {
		WIN, LOSS, PENDING, VOID
	}

	public MarketState getMarketState() {
		return marketState;
	}

	public void setMarketState(MarketState marketState) {
		this.marketState = marketState;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}