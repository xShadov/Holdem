package com.tp.holdem.server;

import com.tp.holdem.logic.model.Deck;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameHandler {
	private final GameParams gameParams;

	private List<Player> players;
	private Deck deck;
	private PokerTable table;

	public GameHandler(GameParams gameParams) {
		this.gameParams = gameParams;

		this.players = List.empty();
		this.deck = Deck.brandNew();
		this.table = PokerTable.builder()
				.bigBlindAmount(gameParams.getBigBlindAmount())
				.smallBlindAmount(gameParams.getSmallBlindAmount())
				.build();
	}

	public Tuple2<List<Player>, PokerTable> startGame() {
		if (players.size() != gameParams.getPlayerCount())
			throw new RuntimeException("Game cannot be started - wrong number of players");

		log.debug(String.format("Starting dto with %d players", gameParams.getPlayerCount()));

		players = players.map(player -> player.toBuilder().inGame(true).betAmount((int) (Math.random() * 1000)).build());

		players = deck.dealCards(2, players);

		return Tuple.of(players, table);
	}

	public Player connectPlayer() {
		final Player newPlayer = numberedPlayer();
		players = players.push(newPlayer);
		return newPlayer;
	}

	private Player numberedPlayer() {
		final Integer newNumber = players
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
