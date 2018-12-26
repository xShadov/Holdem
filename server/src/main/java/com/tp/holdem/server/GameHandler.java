package com.tp.holdem.server;

import com.tp.holdem.logic.model.Deck;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameHandler {
	private final GameParams gameParams;

	private Deck deck;
	private PokerTable table;

	public GameHandler(GameParams gameParams) {
		this.gameParams = gameParams;

		this.deck = Deck.brandNew();
		this.table = PokerTable.builder()
				.bigBlindAmount(gameParams.getBigBlindAmount())
				.smallBlindAmount(gameParams.getSmallBlindAmount())
				.build();
	}

	public PokerTable startGame() {
		if (table.getAllPlayers().size() != gameParams.getPlayerCount())
			throw new RuntimeException("Game cannot be started - wrong number of players");

		log.debug(String.format("Starting dto with %d players", gameParams.getPlayerCount()));

		final List<Player> readyPlayers = table.getAllPlayers()
				.map(player -> player.toBuilder().inGame(true).betAmount((int) (Math.random() * 1000)).build());

		final List<Player> playersWithCards = deck.dealCards(2, readyPlayers);

		table = table.toBuilder()
				.allPlayers(playersWithCards)
				.bettingPlayer(playersWithCards.get(0))
				.build();

		return table;
	}

	public Player connectPlayer() {
		final Player newPlayer = numberedPlayer();
		table = table.addPlayer(newPlayer);
		return newPlayer;
	}

	private Player numberedPlayer() {
		final Integer newNumber = table.getAllPlayers()
				.lastOption()
				.map(Player::getNumber)
				.map(number -> number + 1)
				.getOrElse(0);

		return Player.builder()
				.name("Player" + newNumber)
				.number(newNumber)
				.chipsAmount(gameParams.getStartingChips())
				.build();
	}
}
