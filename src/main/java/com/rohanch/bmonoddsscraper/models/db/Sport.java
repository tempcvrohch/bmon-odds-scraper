package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Column;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"sportName",
		"sportId",
		"wsId",
		"marketTypeEntities"
})
public class Sport {
	@JsonProperty("sportName")
	@Column(name = "sport_name", nullable = false)
	private String sportName;

	@JsonProperty("sportId")
	@Column(name = "sport_id", nullable = false)
	private String sportId;

	@JsonProperty("wsId")
	@Column(name = "ws_id", nullable = false)
	private String wsId;

	@JsonProperty("marketTypeEntities")
	private List<MarketType> marketTypeEntities = null;

	@JsonProperty("sportName")
	public String getSportName() {
		return sportName;
	}

	@JsonProperty("sportName")
	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	@JsonProperty("sportId")
	public String getSportId() {
		return sportId;
	}

	@JsonProperty("sportId")
	public void setSportId(String sportId) {
		this.sportId = sportId;
	}

	@JsonProperty("wsId")
	public String getWsId() {
		return wsId;
	}

	@JsonProperty("wsId")
	public void setWsId(String wsId) {
		this.wsId = wsId;
	}

	@JsonProperty("marketTypeEntities")
	public List<MarketType> getMarketTypeEntities() {
		return marketTypeEntities;
	}

	@JsonProperty("marketTypeEntities")
	public void setMarketTypeEntities(List<MarketType> marketTypeEntities) {
		this.marketTypeEntities = marketTypeEntities;
	}
}
