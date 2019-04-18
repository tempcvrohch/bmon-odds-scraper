package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonInclude;
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
	@Column(nullable = false)
	private String sportName;

	@Column(nullable = false)
	private String sportId;

	@Column(nullable = false)
	private String wsId;

	private List<MarketType> marketTypeEntities = null;

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public String getSportId() {
		return sportId;
	}

	public void setSportId(String sportId) {
		this.sportId = sportId;
	}

	public String getWsId() {
		return wsId;
	}

	public void setWsId(String wsId) {
		this.wsId = wsId;
	}

	public List<MarketType> getMarketTypeEntities() {
		return marketTypeEntities;
	}

	public void setMarketTypeEntities(List<MarketType> marketTypeEntities) {
		this.marketTypeEntities = marketTypeEntities;
	}
}
