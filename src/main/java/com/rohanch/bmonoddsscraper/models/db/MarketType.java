package com.rohanch.bmonoddsscraper.models.db;

import javax.persistence.Column;
import javax.persistence.Id;

public class MarketType {
	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
