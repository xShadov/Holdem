package com.tp.holdem.server;

import com.tp.holdem.logic.HandOperations;
import com.tp.holdem.logic.PlayerFunctions;
import com.tp.holdem.logic.model.Deck;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.common.Phase;
import com.tp.holdem.model.message.PlayerActionMessage;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
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
				.deck(deck)
				.phase(Phase.START)
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
		log.debug("Starting new round");

		this.deck = Deck.brandNew();

		log.debug("Preparing players for new round");
		final List<Player> playersWithCleanBets = table.getAllPlayers().map(Player::newRound);

		log.debug("Dealing cards to players");
		final List<Player> playersWithCards = deck.dealCards(2, playersWithCleanBets);

		final Player dealerPlayer = playersWithCards.get((int) (handCount.get() % playersWithCards.size()));

		final Player smallBlindPlayer = playersWithCards.get((int) (handCount.get() + 1 % playersWithCards.size()));
		log.debug(String.format("Taking small blind from player: %d", smallBlindPlayer.getNumber()));
		final Player newSmallBlindPlayer = PlayerFunctions.SMALL_BLIND_TIME.apply(smallBlindPlayer, table);

		final Player bigBlindPlayer = playersWithCards.get((int) (handCount.get() + 2 % playersWithCards.size()));
		log.debug(String.format("Taking big blind from player: %d", bigBlindPlayer.getNumber()));
		final Player newBigBlindPlayer = PlayerFunctions.BIG_BLIND_TIME.apply(bigBlindPlayer, table);

		this.table = table.toBuilder()
				.deck(this.deck)
				.allPlayers(playersWithCards.replace(smallBlindPlayer, newSmallBlindPlayer).replace(bigBlindPlayer, newBigBlindPlayer))
				.dealer(dealerPlayer)
				.bigBlind(newBigBlindPlayer)
				.smallBlind(newSmallBlindPlayer)
				.build();

		log.debug(String.format("Players ready for new round: %s", table.getAllPlayers().map(Player::getNumber)));

		return startPhase();
	}

	PokerTable startPhase() {
		log.debug(String.format("Staring phase: %s", table.getPhase().nextPhase()));

		log.debug("Preparing table for new phase");
		this.table = this.table.nextPhase();

		return table;
	}

	PokerTable handlePlayerMove(Integer playerNumber, PlayerActionMessage content) throws IllegalArgumentException {
		log.debug(String.format("Handling player %d move: %s", playerNumber, content.getMove()));

		final Player actionPlayer = table.getAllPlayers().find(player -> Objects.equals(player.getNumber(), playerNumber))
				.getOrElseThrow(() -> new IllegalStateException("Player is not in the game"));

		final Player playerAfterAction;

		switch (content.getMove()) {
			case FOLD:
				playerAfterAction = actionPlayer.fold();
				break;
			case ALLIN:
				playerAfterAction = actionPlayer.allIn();
				break;
			case BET:
				playerAfterAction = actionPlayer.bet(content.getBetAmount());
				break;
			case CHECK:
				playerAfterAction = actionPlayer;
				break;
			case CALL:
				playerAfterAction = actionPlayer.bet(table.highestBetThisPhase() - actionPlayer.getBetAmountThisPhase());
				break;
			case RAISE:
				playerAfterAction = actionPlayer.bet(table.highestBetThisPhase() - actionPlayer.getBetAmountThisPhase()).bet(content.getBetAmount());
				break;
			default:
				throw new IllegalArgumentException(String.format("Unsupported action type: %s", content.getMove()));
		}

		this.table = this.table.playerActed(playerAfterAction, content.getMove());

		if (table.isPhaseOver()) {
			log.debug("Table phase is over");
			if (table.getPhase() == Phase.RIVER) {
				log.debug("Table round is over");
				return roundOver();
			}
			return startPhase();
		}

		log.debug("Finding next player to bet");
		this.table = this.table.nextPlayerToBet();

		return this.table;
	}

	private PokerTable roundOver() {
		log.debug("Performing roundOver operation");

		this.table = this.table.toBuilder()
				.winnerPlayer(HandOperations.findWinner(table.getAllPlayers()))
				.phase(Phase.OVER)
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
