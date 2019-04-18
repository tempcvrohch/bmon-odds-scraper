package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"setScore",
		"pointScore",
		"servingIndex",
		"marketStates",
})

@Entity
@Table(name = "matches_states")
public class MatchState extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String setScore;

	@Column(nullable = false)
	private String pointScore;

	@Column(nullable = false)
	private String servingIndex;

	@JsonInclude()
	@Transient
	private List<MarketState> marketStates = null;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "match_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Match match;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSetScore() {
		return setScore;
	}

	public void setSetScore(String setScore) {
		this.setScore = setScore;
	}

	public String getPointScore() {
		return pointScore;
	}

	public void setPointScore(String pointScore) {
		this.pointScore = pointScore;
	}

	public String getServingIndex() {
		return servingIndex;
	}

	public void setServingIndex(String servingIndex) {
		this.servingIndex = servingIndex;
	}

	public List<MarketState> getMarketStates() {
		return marketStates;
	}

	public void setMarketStates(List<MarketState> marketStates) {
		this.marketStates = marketStates;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MatchState that = (MatchState) o;
		return Objects.equals(setScore, that.setScore) &&
				Objects.equals(pointScore, that.pointScore) &&
				Objects.equals(servingIndex, that.servingIndex);
	}

	@Override
	public int hashCode() {
		return Objects.hash(setScore, pointScore, servingIndex);
	}
}
