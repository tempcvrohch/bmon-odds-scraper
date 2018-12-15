package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.MatchEntity;
import com.rohanch.bmonoddsscraper.repositories.MarketStateRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchStateRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class LiveMatchesService {
	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MatchStateRepository matchStateRepository;

	@Autowired
	private MarketStateRepository marketStateRepository;
	private ArrayList<MatchEntity> persistedMatchEntities = new ArrayList<>();

	public ArrayList<MatchEntity> getPersistedMatchEntities() {
		return persistedMatchEntities;
	}

	public void setPersistedMatchEntities(ArrayList<MatchEntity> persistedMatchEntities) {
		this.persistedMatchEntities = persistedMatchEntities;
	}

	/**
	 * New matches are persisted.
	 * Compares updatedMatches with the cached persistedMatches and upserts new Match/Market states if changed.
	 *
	 * @param updatedMatches The currently scraped list of matches.
	 */
	@Transactional
	public void UpdateMatches(MatchEntity[] updatedMatches) {
		var created = 0;
		var updated = 0;

		for (var updatedMatch : updatedMatches) {
			var preparedMatch = prepareMatchEntityForDB(updatedMatch); //TODO: updatedMatch = prepareMatchEntityForDB(updatedMatch); ?
			var optMatch = persistedMatchEntities
					.stream()
					.filter(fMatch -> fMatch.equals(preparedMatch))
					.findFirst();

			if (optMatch.isPresent()) {
				//TODO: Not setting the Id causes a new match to be added even though at this point a match never gets persisted
				updateMatchNestedElements(optMatch.get(), preparedMatch);
				updated++;
			} else {
				persistNewMatch(preparedMatch);
				created++;
			}
		}

		System.out.printf("UpdateMatches: %d matches created and %d matches updated.\n", created, updated);
	}

	/**
	 * Sets a match's multiple nested entities(MatchState and MarketState) to each reference their parents for JPA.
	 *
	 * @param match to be updated.
	 * @return the updated match with referenced parents.
	 */
	private MatchEntity prepareMatchEntityForDB(@NotNull MatchEntity match) {
		match.getMatchState().setMatchEntity(match); //TODO: dicuss entity structure with Deniz
		for (var marketState : match.getMatchState().getMarketStates()) {
			marketState.setMatchState(match.getMatchState());
		}

		return match;
	}

	private void persistNewMatch(@NotNull MatchEntity newMatch) {
		var insertedMatch = matchRepository.save(newMatch); //TODO: why aren't the original objects updated?
		insertedMatch.setMatchState(matchStateRepository.save(insertedMatch.getMatchState()));
		insertedMatch.getMatchState().setMarketStates(marketStateRepository.saveAll(insertedMatch.getMatchState().getMarketStates()));
		persistedMatchEntities.add(insertedMatch);
	}

	/**
	 * Checks if either market states have changed between the current and new match update, if so persist.
	 * Checks if the match state has changed between the current and new match update, if so persist.
	 * <p>
	 * Sets the new match state as the current match state(eg a point score of 0-0 -> 0-1).
	 * <p>
	 * Both params should refer to the same match (eg persistedMatch.name = "PSV vs Ajax" and updatedMatch.name = "PSV vs Ajax").
	 *
	 * @param persistedMatch current persisted match
	 * @param updatedMatch   the updated match
	 */
	private void updateMatchNestedElements(@NotNull MatchEntity persistedMatch, @NotNull MatchEntity updatedMatch) {
		if (!persistedMatch.getMatchState().equals(updatedMatch.getMatchState())) {
			persistedMatch.setMatchState(updatedMatch.getMatchState());
			matchStateRepository.saveAndFlush(persistedMatch.getMatchState());
		}

		//TODO: should these be in their own functions?
		if (!persistedMatch.getMatchState().getMarketStates().equals(updatedMatch.getMatchState().getMarketStates())) {
			persistedMatch.getMatchState().setMarketStates(updatedMatch.getMatchState().getMarketStates()); //TODO: looks ugly
			marketStateRepository.saveAll(persistedMatch.getMatchState().getMarketStates());
		}
	}
}
