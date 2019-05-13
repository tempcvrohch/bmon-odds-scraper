package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
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
