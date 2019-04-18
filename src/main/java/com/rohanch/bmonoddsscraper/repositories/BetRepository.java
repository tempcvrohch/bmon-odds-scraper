package com.rohanch.bmonoddsscraper.repositories;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BetRepository extends JpaRepository<Bet, Long> {
	@Query(value = "SELECT * FROM bets b WHERE b.market_state_id = :marketStateId AND b.user_id = :userId", nativeQuery = true)
	Bet GetBetOnMarketStateIdAndUserId(@Param("marketStateId") Long marketStateId, @Param("userId") Long userId);

	@Query(value = "SELECT * FROM bets b WHERE b.user_id = :userId ORDER BY b.created_at DESC LIMIT 25", nativeQuery = true)
	Bet[] GetBetsPendingOnUserId(@Param("userId") Long userId);

	@Query(value = "SELECT COUNT(id) FROM bets b WHERE b.user_id = :userId AND b.status = :betStatus", nativeQuery = true)
	long GetAmountBetsPendingOnUserId(@Param("userId") Long userId, @Param("betStatus") String betStatus);

	long countByUserIdAndStatus(@Param("userId") Long userId, @Param("betStatus") Bet.BetStatus betStatus);
}
