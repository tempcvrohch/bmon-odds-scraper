package com.rohanch.bmonoddsscraper.repositories;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
	@Query(value = "SELECT * FROM bets b WHERE b.market_state_id = :marketStateId AND b.user_id = :userId", nativeQuery = true)
	Bet getBetOnMarketStateIdAndUserId(@Param("marketStateId") Long marketStateId, @Param("userId") Long userId);

	@Query(value = "SELECT * FROM bets b WHERE b.user_id = :userId ORDER BY b.created_at DESC LIMIT 25", nativeQuery = true)
	Bet[] getBetsPendingOnUserId(@Param("userId") Long userId);

	@Query(value = "SELECT COUNT(id) FROM bets b WHERE b.user_id = :userId AND b.status = :status", nativeQuery = true)
	long getAmountBetsPendingOnUserId(@Param("userId") Long userId, @Param("status") String status);

	long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

	//3 joins bad design?
	@Query(value = "SELECT mts.* FROM matches m " +
			"JOIN matches_states ms ON m.id = ms.match_id " +
			"JOIN market_states mts ON ms.id = mts.match_state_id " +
			"JOIN bets bts ON mts.id = bts.market_state_id " +
			"WHERE m.id = :matchId", nativeQuery = true)
	List<Bet> findBetsByMatchId(@Param("matchId") Long matchId);

	@Modifying
	@Query(value = "UPDATE bets SET status = :status WHERE id = :id", nativeQuery = true)
	void updateBetOnBetStatusById(@Param("id") Long id, @Param("status") Bet.BetStatus status);
}
