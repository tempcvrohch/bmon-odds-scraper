package com.rohanch.bmonoddsscraper.repositories;

import com.rohanch.bmonoddsscraper.models.db.MatchState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchStateRepository extends JpaRepository<MatchState, Long> {
	@Query(value = "SELECT * FROM matches_states m WHERE m.match_id = :matchId", nativeQuery = true)
	MatchState[] findAllByMatchId(@Param("matchId") Long matchId);
}
