package com.rohanch.bmonoddsscraper.repositories;

import com.rohanch.bmonoddsscraper.models.db.MatchStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchStateRepository extends JpaRepository<MatchStateEntity, Long> {
}
