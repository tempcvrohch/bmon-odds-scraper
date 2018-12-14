package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"setScore",
		"pointScore",
		"servingIndex",
		"marketStates",
})

@Entity
@Table(name = "matches_states")
public class MatchStateEntity extends BaseEntity {
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
	private List<MarketStateEntity> marketStates = null;

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "match_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private MatchEntity matchEntity;

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
	public List<MarketStateEntity> getMarketStates() {
		return marketStates;
	}

	@JsonProperty("marketStates")
	public void setMarketStates(List<MarketStateEntity> marketStates) {
		this.marketStates = marketStates;
	}
}
