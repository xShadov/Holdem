package com.tp.holdem.server;

import com.tp.holdem.logic.model.Deck;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
class GameHandler {
	private final GameParams gameParams;

	private Deck deck;
	private PokerTable table;
	private AtomicLong handCount = new AtomicLong(0);

	GameHandler(GameParams gameParams) {
		this.gameParams = gameParams;

		this.deck = Deck.brandNew();
		this.table = PokerTable.builder()
				.bigBlindAmount(gameParams.getBigBlindAmount())
				.smallBlindAmount(gameParams.getSmallBlindAmount())
				.build();
	}

	PokerTable startGame() {
		if (table.getAllPlayers().size() != gameParams.getPlayerCount())
			throw new RuntimeException("Game cannot be started - wrong number of players");

		log.debug(String.format("Starting game with %d players", gameParams.getPlayerCount()));

		final List<Player> readyPlayers = table.getAllPlayers()
				.map(player -> player
						.toBuilder()
						.inGame(true)
						.chipsAmount(gameParams.getStartingChips())
						.build()
				);

		this.table = table.toBuilder()
				.allPlayers(readyPlayers)
				.build();

		return startRound();
	}

	PokerTable startRound() {
		log.debug("Starting round");

		this.deck = Deck.brandNew();

		final List<Player> playersWithCards = deck.dealCards(2, table.getAllPlayers());

		final Player dealerPlayer = playersWithCards.get((int) (handCount.get() % playersWithCards.size()));

		final Player smallBlindPlayer = playersWithCards.get((int) (handCount.get() + 1 % playersWithCards.size()));
		final Player newSmallBlindPlayer = PlayerFunctions.SMALL_BLIND_TIME.apply(smallBlindPlayer, table);

		final Player bigBlindPlayer = playersWithCards.get((int) (handCount.get() + 2 % playersWithCards.size()));
		final Player newBigBlindPlayer = PlayerFunctions.BIG_BLIND_TIME.apply(bigBlindPlayer, table);

		final PokerTable updatedTable = table.toBuilder()
				.allPlayers(playersWithCards.replace(smallBlindPlayer, newSmallBlindPlayer).replace(bigBlindPlayer, newBigBlindPlayer))
				.dealer(dealerPlayer)
				.bigBlind(newBigBlindPlayer)
				.smallBlind(newSmallBlindPlayer)
				.build();

		final Player bettingPlayer = PlayerFunctions.ROUND_START_BETTING_TIME.apply(newSmallBlindPlayer, updatedTable);

		this.table = updatedTable.toBuilder()
				.allPlayers(updatedTable.getAllPlayers().replace(newSmallBlindPlayer, bettingPlayer))
				.bettingPlayer(bettingPlayer)
				.build();

		return table;
	}

	Player connectPlayer() {
		final Player newPlayer = numberedPlayer();
		table = table.addPlayer(newPlayer);
		return newPlayer;
	}

	private Player numberedPlayer() {
		return table.getAllPlayers()
				.lastOption()
				.map(Player::getNumber)
				.map(number -> number + 1)
				.map(Player::numbered)
				.getOrElse(() -> Player.numbered(0));
	}

	PokerTable disconnectPlayer(Integer playerNumber) {
		table = table.playerLeft(playerNumber);
		return table;
	}
}
