
package com.rohanch.bmonoddsscraper.models;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"name",
		"idFi",
		"idId",
		"suspended",
		"odd"
})
public class Player {

	@JsonProperty("name")
	private String name;
	@JsonProperty("idFi")
	private String idFi;
	@JsonProperty("idId")
	private String idId;
	@JsonProperty("suspended")
	private String suspended;
	@JsonProperty("odd")
	private String odd;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("idFi")
	public String getIdFi() {
		return idFi;
	}

	@JsonProperty("idFi")
	public void setIdFi(String idFi) {
		this.idFi = idFi;
	}

	@JsonProperty("idId")
	public String getIdId() {
		return idId;
	}

	@JsonProperty("idId")
	public void setIdId(String idId) {
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
