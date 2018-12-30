package com.tp.holdem.server;

import com.tp.holdem.logic.model.PhaseStatus;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PlayerNumber;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.common.Phase;
import com.tp.holdem.model.message.PlayerActionMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
class GameHandler {
	private final GameParams gameParams;

	private PokerTable table;
	private AtomicLong handCount = new AtomicLong(-1);

	GameHandler(GameParams gameParams) {
		this.gameParams = gameParams;

		this.table = PokerTable.withBlinds(gameParams.getBigBlindAmount(), gameParams.getSmallBlindAmount());
	}

	PokerTable startGame() {
		if (table.getAllPlayers().size() != gameParams.getPlayerCount())
			throw new IllegalStateException("Game cannot be started - wrong number of players");

		log.debug(String.format("Starting game with %d players", gameParams.getPlayerCount()));

		this.table = table.preparePlayersForNewGame(gameParams.getStartingChips());

		return startRound();
	}

	PokerTable startRound() {
		if (this.table.isNotPlayable()) {
			log.debug("Game is over");
			this.table = table.gameOver();
			return this.table;
		}

		handCount.incrementAndGet();

		log.debug(String.format("Starting new round number: %d", handCount.get()));

		this.table = table.newRound(handCount);

		log.debug(String.format("Players ready for new round: %s", table.getAllPlayers().map(Player::getName)));

		return startPhase();
	}

	PokerTable startPhase() {
		log.debug(String.format("Staring phase: %s", table.getPhase().nextPhase()));

		this.table = table.nextPhase();

		return table;
	}

	PokerTable handlePlayerMove(Integer playerNumber, PlayerActionMessage content) throws IllegalArgumentException {
		log.debug(String.format("Handling player %d move: %s", playerNumber, content.getMove()));

		this.table = table.playerMove(playerNumber, content.getMove(), content.getBetAmount());

		final PhaseStatus phaseStatus = table.phaseStatus();

		log.debug(String.format("Phase status is: %s", phaseStatus));

		if (phaseStatus == PhaseStatus.EVERYBODY_FOLDED || (phaseStatus == PhaseStatus.READY_FOR_NEXT && table.getPhase() == Phase.RIVER)) {
			log.debug("Table round is over");
			return roundOver();
		}

		if (phaseStatus == PhaseStatus.EVERYBODY_ALL_IN) {
			this.table = table.showdownMode();
			return this.table;
		}

		if (phaseStatus == PhaseStatus.READY_FOR_NEXT) {
			log.debug("Table phase is over");
			return startPhase();
		}

		log.debug("Finding next player to bet");
		this.table = table.nextPlayerToBet();

		return this.table;
	}

	PokerTable roundOver() {
		log.debug("Performing round-over operation");
		this.table = table.roundOver();

		return table;
	}

	Player connectPlayer() {
		log.debug("Connecting new player");
		final Player newPlayer = numberedPlayer();
		this.table = table.addPlayer(newPlayer);
		return newPlayer;
	}

	PokerTable disconnectPlayer(Integer playerNumber) {
		log.debug(String.format("Disconnecting player: %d", playerNumber));
		this.table = table.playerLeft(PlayerNumber.of(playerNumber));
		return table;
	}

	private Player numberedPlayer() {
		return table.getAllPlayers()
				.lastOption()
				.map(Player::getNumber)
				.map(number -> number + 1)
				.map(Player.Companion::numbered)
				.getOrElse(() -> Player.Companion.numbered(0));
	}
}
