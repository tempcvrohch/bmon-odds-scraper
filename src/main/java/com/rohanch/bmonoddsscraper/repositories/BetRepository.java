package com.rohanch.bmonoddsscraper.repositories;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BetRepository extends JpaRepository<Bet, Long> {
	@Query(value = "SELECT * FROM bets b WHERE b.market_state_id = :marketStateId AND b.user_id = :userId", nativeQuery = true)
	Bet GetBetOnMarketStateIdAndUserId(@Param("marketStateId") Long marketStateId, @Param("userId") Long userId);
}
