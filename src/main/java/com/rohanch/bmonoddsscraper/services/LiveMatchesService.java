package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MarketStateRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchStateRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class LiveMatchesService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MatchStateRepository matchStateRepository;

	@Autowired
	private MarketStateRepository marketStateRepository;
	private ArrayList<Match> persistedMatchEntities = new ArrayList<>();

	public ArrayList<Match> getPersistedMatchEntities() {
		return persistedMatchEntities;
	}

	public void setPersistedMatchEntities(ArrayList<Match> persistedMatchEntities) {
		this.persistedMatchEntities = persistedMatchEntities;
	}

	/**
	 * New matches are persisted.
	 * Compares updatedMatches with the cached persistedMatches and upserts new Match/Market states if changed.
	 *
	 * @param updatedMatches The currently scraped list of matches.
	 */
	@Transactional
	void UpdateMatches(Match[] updatedMatches) {
		var created = 0;
		var updated = 0;

		if (persistedMatchEntities.size() == 0) {
			loadInitialLiveMatchesFromDB();
		}

		logger.debug("Upserting {} matches.", updatedMatches.length);

		for (var updatedMatch : updatedMatches) {
			var optMatch = persistedMatchEntities
					.stream()
					.filter(fMatch -> fMatch.equals(updatedMatch))
					.findFirst();

			if (optMatch.isPresent()) {
				updateMatchNestedElements(optMatch.get(), updatedMatch);
				updated++;
			} else {
				persistNewMatch(updatedMatch);
				created++;
			}
		}

		logger.debug("{} matches created and {} matches updated.", created, updated);
	}

	/**
	 * In case of an application restart the persisted matches variable should be consistent with what is in the database.
	 */
	private void loadInitialLiveMatchesFromDB() {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -1);
		persistedMatchEntities = new ArrayList<>(matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis())));
	}

	/**
	 * Sets a match's multiple nested entities(MatchState and MarketState) to each reference their parents for JPA.
	 *
	 * @param match to be updated.
	 * @return the updated match with referenced parents.
	 */
	private Match prepareMatchEntityForDB(@NotNull Match match, @NotNull Match targetMatch) {
		match.getMatchState().setMatch(targetMatch);
		for (var marketState : match.getMatchState().getMarketStates()) {
			marketState.setMatchState(targetMatch.getMatchState());
		}

		return match;
	}

	private void persistNewMatch(@NotNull Match newMatch) {
		var preparedMatch = prepareMatchEntityForDB(newMatch, newMatch); //TODO: ugly

		var insertedMatch = matchRepository.save(preparedMatch);
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
	private void updateMatchNestedElements(@NotNull Match persistedMatch, @NotNull Match updatedMatch) { //TODO: this whole thing feels confusing
		var preparedMatch = prepareMatchEntityForDB(updatedMatch, persistedMatch);

		if (!persistedMatch.getMatchState().equals(preparedMatch.getMatchState())) {
			persistedMatch.setMatchState(preparedMatch.getMatchState());
			matchStateRepository.saveAndFlush(persistedMatch.getMatchState());
		}

		//TODO: should these be in their own functions?
		if (!persistedMatch.getMatchState().getMarketStates().equals(preparedMatch.getMatchState().getMarketStates())) {
			persistedMatch.getMatchState().setMarketStates(preparedMatch.getMatchState().getMarketStates());
			marketStateRepository.saveAll(persistedMatch.getMatchState().getMarketStates());
		}
	}
}
