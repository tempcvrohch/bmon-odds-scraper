package com.rohanch.bmonoddsscraper.models;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
