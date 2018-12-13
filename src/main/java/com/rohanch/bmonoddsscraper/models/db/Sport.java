package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"sportName",
		"sportId",
		"wsId",
		"marketTypes"
})
public class Sport {
	@JsonProperty("sportName")
	private String sportName;
	@JsonProperty("sportId")
	private String sportId;
	@JsonProperty("wsId")
	private String wsId;
	@JsonProperty("marketTypes")
	private List<MarketType> marketTypes = null;

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

	@JsonProperty("marketTypes")
	public List<MarketType> getMarketTypes() {
		return marketTypes;
	}

	@JsonProperty("marketTypes")
	public void setMarketTypes(List<MarketType> marketTypes) {
		this.marketTypes = marketTypes;
	}
}
