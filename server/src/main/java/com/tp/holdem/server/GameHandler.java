package com.tp.holdem.server;

import com.google.common.collect.Lists;
import com.tp.holdem.model.game.Deck;
import com.tp.holdem.model.game.Player;
import com.tp.holdem.model.game.PokerTable;
import com.tp.holdem.model.message.UpdateStateMessage;
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
		this.deck = Deck.instance();
		this.table = PokerTable.builder()
				.bigBlindAmount(gameParams.getBigBlindAmount())
				.smallBlindAmount(gameParams.getSmallBlindAmount())
				.cardsOnTable(Lists.newArrayList())
				.build();
	}

	public UpdateStateMessage startGame() {
		if (players.size() != gameParams.getPlayerCount())
			throw new RuntimeException("Game cannot be started - wrong number of players");

		log.debug(String.format("Starting game with %d players", gameParams.getPlayerCount()));

		players.forEach(player -> player.setInGame(true));
		players.forEach(player -> player.setBetAmount((int) (Math.random() * 1000)));

		deck.dealCards(2, players);

		return UpdateStateMessage.builder()
				.bettingPlayer(players.get(1))
				.allPlayers(players.toJavaList())
				.table(table)
				.build();
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
