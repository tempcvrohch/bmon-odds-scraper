package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.repositories.MarketStateRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchRepository;
import com.rohanch.bmonoddsscraper.repositories.MatchStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class LiveMatchesService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MatchStateRepository matchStateRepository;

	@Autowired
	private MarketStateRepository marketStateRepository;
	private ArrayList<Match> persistedMatchEntities = new ArrayList<>();

	private List<Match> liveMatchEntities = new ArrayList<>();

	public List<Match> getLiveMatchEntities() {
		return liveMatchEntities;
	}

	/**
	 * New matches are persisted.
	 * Compares updatedMatches with the cached persistedMatches and upserts new Match/Market states if changed.
	 *
	 * @param updatedMatches The currently scraped list of matches.
	 */
//	@Transactional
	void UpdateMatches(Match[] updatedMatches) {
		liveMatchEntities = Arrays.asList(updatedMatches);
		var created = 0;
		var updated = 0;

		if (persistedMatchEntities.isEmpty()) {
			persistedMatchEntities = getRecentMatchesFromDB();
		}

		logger.debug("Checking {} matches.", updatedMatches.length);

		for (var updatedMatch : updatedMatches) {
			var optMatch = persistedMatchEntities
					.stream()
					.filter(fMatch -> fMatch.equals(updatedMatch))
					.findFirst();

			if (optMatch.isPresent()) {
				var changed = updateMatchNestedElements(optMatch.get(), updatedMatch);
				if (changed) {
					updated++;
				}
			} else {
				persistNewMatch(updatedMatch);
				created++;
			}
		}

		if (created > 0 || updated > 0) {
			logger.debug("{} matches created and {} matches updated.", created, updated);
		}
	}

	/**
	 * In case of an application restart the persisted matches variable should be consistent with what is in the database.
	 */
	private ArrayList<Match> getRecentMatchesFromDB() {
		var calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -1);
		return new ArrayList<>(matchRepository.findAfterTimestamp(new Timestamp(calendar.getTimeInMillis())));
	}

	/**
	 * Sets a match's multiple nested entities(MatchState and MarketState) to each reference their parents for JPA.
	 *
	 * @param match to be updated.
	 * @return the updated match with referenced parents.
	 */
	private Match setParentReferencesOnChildren(Match match, Match targetMatch) {
		match.getMatchState().setMatch(targetMatch);
		for (var marketState : match.getMatchState().getMarketStates()) {
			marketState.setMatchState(targetMatch.getMatchState());
		}

		return match;
	}

	private void persistNewMatch(Match newMatch) {
		var preparedMatch = setParentReferencesOnChildren(newMatch, newMatch);

		//the match object needs a db reference to the latest matchState(which isn't set yet) so unset it first
		var transientMatchState = preparedMatch.getMatchState();
		preparedMatch.setMatchState(null);

		var insertedMatch = matchRepository.save(preparedMatch);
		transientMatchState = matchStateRepository.save(transientMatchState);

		//the match state has been persisted and can now be saved to the match
		insertedMatch.setMatchState(transientMatchState);
		insertedMatch = matchRepository.save(insertedMatch);

		var insertedMarketStates = marketStateRepository.saveAll(newMatch.getMatchState().getMarketStates());
		insertedMatch.getMatchState().setMarketStates(insertedMarketStates);
		persistedMatchEntities.add(insertedMatch);
	}

	/**
	 * Checks if either market states have changed between the current and new match update, if so persist.
	 * Checks if the match state has changed between the current and new match update, if so persist.
	 * <p>
	 * Sets the new match state as the current match state(eg a point score of 0-0 -> 0-1).
	 * <p>
	 * Both params should refer to the same match (eg persistedMatch.name = "PSV vs Ajax" and updatedMatch.name = "PSV vs Ajax").
	 *  @param persistedMatch current persisted match
	 * @param updatedMatch   the updated match
	 */
	private boolean updateMatchNestedElements(Match persistedMatch, Match updatedMatch) { //TODO: this whole thing feels confusing
		var changed = false;
		var preparedMatch = setParentReferencesOnChildren(updatedMatch, persistedMatch);

		if (!persistedMatch.getMatchState().equals(preparedMatch.getMatchState())) {
			persistedMatch.setMatchState(preparedMatch.getMatchState());
			var insertedMatchState = matchStateRepository.save(persistedMatch.getMatchState()); //save the new match state

			persistedMatch.setMatchState(insertedMatchState);
			matchRepository.save(persistedMatch); //save the reference to the new match state to the match object
			changed = true;
		}

		//TODO: should these be in their own functions?
		if (!persistedMatch.getMatchState().getMarketStates().equals(preparedMatch.getMatchState().getMarketStates())) {
			persistedMatch.getMatchState().setMarketStates(preparedMatch.getMatchState().getMarketStates());
			marketStateRepository.saveAll(persistedMatch.getMatchState().getMarketStates());
			changed = true;
		}

		return changed;
	}
}
