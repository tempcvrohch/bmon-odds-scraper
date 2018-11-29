package com.rohanch.bmonoddsscraper.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"name",
		"idFi",
		"idId",
		"suspended",
		"odd"
})
public class MarketState {

	@JsonProperty("marketName")
	private String marketName;
	@JsonProperty("name")
	private String name;
	@JsonProperty("idFi")
	private Long idFi;
	@JsonProperty("idId")
	@Id
	private Long idId;
	@JsonProperty("suspended")
	private String suspended;
	@JsonProperty("odd")
	private String odd;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "match_state_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private MatchState matchState;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("idFi")
	public Long getIdFi() {
		return idFi;
	}

	@JsonProperty("idFi")
	public void setIdFi(Long idFi) {
		this.idFi = idFi;
	}

	@JsonProperty("idId")
	public Long getIdId() {
		return idId;
	}

	@JsonProperty("idId")
	public void setIdId(Long idId) {
		this.idId = idId;
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

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
}
