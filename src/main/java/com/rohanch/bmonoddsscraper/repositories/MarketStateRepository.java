package com.rohanch.bmonoddsscraper.repositories;

import com.rohanch.bmonoddsscraper.models.db.MarketState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketStateRepository extends JpaRepository<MarketState, Long> {
	//TODO: there is probably a better way of doing this.
	@Query(value = "SELECT mts.* FROM matches m JOIN matches_states ms ON m.id = ms.match_id JOIN market_states mts ON ms.id = mts.match_state_id WHERE m.id = :matchId ORDER BY mts.id DESC LIMIT 2 ", nativeQuery = true)
	MarketState[] findLatestMarketStatesOnMatchId(@Param("matchId") Long matchId);
}