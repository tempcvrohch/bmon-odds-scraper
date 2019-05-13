package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "bets")
@Data
public class Bet extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BetStatus status;

	@Column(nullable = false)
	private Float stake;

	@Column(nullable = false)
	private Float toReturn;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "market_state_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private MarketState marketState;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	public enum BetStatus {
		WIN, LOSS, PENDING, VOID
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	@JsonProperty("user")
	public void setUser(User user) {
		this.user = user;
	}
}
