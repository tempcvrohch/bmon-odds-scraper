package com.rohanch.bmonoddsscraper.models.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class MarketType {
	@Id
	private String id;

	@Column(nullable = false)
	private String name;
}
