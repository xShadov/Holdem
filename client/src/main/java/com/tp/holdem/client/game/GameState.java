package com.tp.holdem.client.game;

import com.google.common.base.Preconditions;
import com.tp.holdem.client.architecture.bus.Action;
import com.tp.holdem.client.architecture.bus.GameObservable;
import com.tp.holdem.client.architecture.model.action.ActionType;
import com.tp.holdem.client.architecture.model.common.PlayerConnectMessage;
import com.tp.holdem.client.architecture.model.common.UpdateStateMessage;
import com.tp.holdem.client.model.Card;
import com.tp.holdem.client.model.Player;
import com.tp.holdem.client.model.PokerTable;
import io.vavr.control.Option;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameState implements GameObservable {
	private PokerTable table;
	private Player currentPlayer;
	private Player bettingPlayer;
	private Player winnerPlayer;
	private List<Player> allPlayers;

	public List<Player> getOtherPlayers() {
		return getAllPlayers().stream()
				.skip(1)
				.collect(Collectors.toList());
	}

	public List<Player> playersExcept(Player... players) {
		final List<Integer> numbers = Arrays.stream(players).map(Player::getNumber).collect(Collectors.toList());
		return getAllPlayers().stream()
				.filter(player -> !numbers.contains(player.getNumber()))
				.collect(Collectors.toList());
	}

	public List<Player> getAllPlayers() {
		final List<Player> players = allPlayers;

		while (players.get(0).getNumber() != currentPlayer.getNumber())
			Collections.rotate(players, 1);

		return players;
	}

	public Option<PokerTable> getTable() {
		return Option.of(table);
	}

	public Option<Player> getBettingPlayer() {
		return Option.of(bettingPlayer);
	}

	public Option<Player> getWinnerPlayer() {
		return Option.of(winnerPlayer);
	}

	public boolean isCurrentPlayerWinner() {
		return getWinnerPlayer()
				.exists(player -> player.getNumber() == currentPlayer.getNumber());
	}

	public boolean isCurrentPlayerWaiting() {
		return isCurrentPlayerConnected() && !isGameStarted();
	}

	public boolean isCurrentPlayerConnected() {
		return currentPlayer != null;
	}

	public boolean isGameStarted() {
		return getTable().isDefined();
	}

	public int relativePlayerNumber(Player player) {
		return (player.getNumber() + getCurrentPlayer().getNumber()) % getAllPlayers().size();
	}

	public boolean hasWinner() {
		return getWinnerPlayer().isDefined();
	}

	public boolean isSomeoneBetting() {
		return getBettingPlayer().isDefined();
	}

	public List<Card> getCardsOnTable() {
		return table.getCardsOnTable();
	}

	public int getPotAmount() {
		return table.getPotAmount();
	}

	public int getSmallBlindAmount() {
		return table.getSmallBlindAmount();
	}

	public int getBigBlindAmount() {
		return table.getBigBlindAmount();
	}

	@Override
	public void accept(Action action) {
		log.debug(String.format("Received action: %s", action.getActionType()));

		if (action.getActionType() == ActionType.PLAYER_CONNECT) {
			handlePlayerConnection(action);
		}

		if (action.getActionType() == ActionType.UPDATE_STATE) {
			handleUpdateState(action);
		}
	}

	private void handlePlayerConnection(Action action) {
		log.debug("Handling player connection event");

		Preconditions.checkArgument(!isCurrentPlayerConnected(), "Player already connected");

		final PlayerConnectMessage response = action.instance(PlayerConnectMessage.class);

		currentPlayer = response.getPlayer();

		log.debug(String.format("Registered current player with number: %d", currentPlayer.getNumber()));
	}

	private void handleUpdateState(Action action) {
		log.debug("Handling update state event");

		final UpdateStateMessage response = action.instance(UpdateStateMessage.class);
		copyProperties(response.getGameState());

		log.debug(String.format("Staring game with %d players", allPlayers.size()));
	}

	public void copyProperties(GameState state) {
		this.currentPlayer = state.currentPlayer;
		this.allPlayers = state.allPlayers;
		this.bettingPlayer = state.bettingPlayer;
		this.table = state.table;
		this.winnerPlayer = state.winnerPlayer;
	}
}
