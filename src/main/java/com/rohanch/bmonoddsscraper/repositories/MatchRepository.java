package com.rohanch.bmonoddsscraper.repositories;

import com.rohanch.bmonoddsscraper.models.db.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
	@Query(value = "SELECT * FROM matches m WHERE m.created_at >= :timestamp", nativeQuery = true)
	List<MatchEntity> findAfterTimestamp(@Param("timestamp") Timestamp timestamp);
}