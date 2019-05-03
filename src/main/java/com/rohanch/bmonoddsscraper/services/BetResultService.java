package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.Bet;
import com.rohanch.bmonoddsscraper.models.db.Match;
import com.rohanch.bmonoddsscraper.models.db.MatchState;
import com.rohanch.bmonoddsscraper.repositories.BetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BetResultService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BetRepository betRepository;

	@Autowired
	private BetService betService;

	void ProcessUserBetsOnMatch(Match match) {
		var pendingBets = betRepository.FindBetsByMatchId(match.getId());
		if (pendingBets.size() == 0) {
			return;
		}

		var sideScoresArray = extractLastSetPlayerScores(match.getMatchState());
		var winnerIndex = parseWinnerIndex(sideScoresArray[0], sideScoresArray[1]);

		pendingBets.forEach(bet -> processFinishedMatchBet(match, bet, winnerIndex));
	}

	private void processFinishedMatchBet(Match match, Bet bet, int winnerIndex) {
		if (winnerIndex == -1) {
			betService.ProcessVoidBet(bet);
		} else {
			var winningPlayerName = match.getMatchState().getMarketStates().get(winnerIndex).getPlayerName();
			betService.ProcessFinishedBet(bet, bet.getMarketState().getPlayerName().equals(winningPlayerName));
		}
	}

	/**
	 * @param matchState a valid matchState
	 * @return an int array with 2 elements, the leftSideScore and rightSideScore
	 */
	private int[] extractLastSetPlayerScores(MatchState matchState) {
		var splitSetScore = matchState.getSetScore().split(",");

		var splitLastSetScore = splitSetScore[splitSetScore.length - 1].split("-");
		var leftSideScore = Integer.parseInt(splitLastSetScore[0]);
		var rightSideScore = Integer.parseInt(splitLastSetScore[1]);

		return new int[]{leftSideScore, rightSideScore};
	}

	/**
	 * @param leftSideScore  int between 0-7
	 * @param rightSideScore int between 0-7
	 * @return Returns the index of the winning player, or -1 if the match isn't finished yet.
	 */
	private int parseWinnerIndex(int leftSideScore, int rightSideScore) {
		if (leftSideScore == 7) {
			return 0;
		} else if (rightSideScore == 7) {
			return 1;
		}

		if (leftSideScore >= 6 && leftSideScore - 2 >= rightSideScore) {
			return 0;
		} else if (rightSideScore >= 6 && rightSideScore - 2 >= leftSideScore) {
			return 1;
		} else {
			return -1;
		}
	}
}
