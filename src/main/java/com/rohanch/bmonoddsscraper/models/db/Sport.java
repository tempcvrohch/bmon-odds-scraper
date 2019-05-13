package com.rohanch.bmonoddsscraper.models.db;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class Sport {
	@Column(nullable = false)
	private String sportName;

	@Column(nullable = false)
	private String sportId;

	@Column(nullable = false)
	private String wsId;

	private List<MarketType> marketTypeEntities = null;
}
