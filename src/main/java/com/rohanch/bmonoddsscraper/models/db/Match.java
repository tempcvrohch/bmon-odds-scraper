package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "matches")
public class Match extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true, nullable = false)
	@JsonProperty("bId")
	private String bId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String sportName;

	@Column(nullable = false)
	private String leagueName;

	@JsonInclude()
	@OneToOne()
	private MatchState matchState = null;

	@JsonInclude()
	@Transient
	private MatchState[] matchStates = null;

	@Transient
	@JsonInclude()
	private boolean live;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Match that = (Match) o;
		return Objects.equals(bId, that.bId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bId);
	}
}
