package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
	@JsonProperty("id")
	private Long id;

	@JsonProperty("setScore")
	@Column(name = "set_score", nullable = false)
	private String setScore;

	@JsonProperty("pointScore")
	@Column(name = "point_score", nullable = false)
	private String pointScore;

	@JsonProperty("servingIndex")
	@Column(name = "serving_index", nullable = false)
	private String servingIndex;

	@JsonInclude()
	@Transient
	@JsonProperty("marketStates")
	private List<MarketState> marketStates = null;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "match_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Match match;

	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty("setScore")
	public String getSetScore() {
		return setScore;
	}

	@JsonProperty("setScore")
	public void setSetScore(String setScore) {
		this.setScore = setScore;
	}

	@JsonProperty("pointScore")
	public String getPointScore() {
		return pointScore;
	}

	@JsonProperty("pointScore")
	public void setPointScore(String pointScore) {
		this.pointScore = pointScore;
	}

	@JsonProperty("servingIndex")
	public String getServingIndex() {
		return servingIndex;
	}

	@JsonProperty("servingIndex")
	public void setServingIndex(String servingIndex) {
		this.servingIndex = servingIndex;
	}

	@JsonProperty("marketStates")
	public List<MarketState> getMarketStates() {
		return marketStates;
	}

	@JsonProperty("marketStates")
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
