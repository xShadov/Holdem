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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@NoArgsConstructor
public class GameState implements GameObservable {
	private PokerTable table;
	private Player currentPlayer;
	private List<Player> allPlayers;

	public List<Player> getOtherPlayers() {
		return getAllPlayers().stream()
				.filter(player -> player.getNumber() != getCurrentPlayer().getNumber())
				.collect(Collectors.toList());
	}

	public boolean isCurrentPlayerWaiting() {
		return isCurrentPlayerConnected() && !isGameStarted();
	}

	public boolean isCurrentPlayerConnected() {
		return currentPlayer != null;
	}

	public boolean isGameStarted() {
		return table != null;
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

		currentPlayer = response.getCurrentPlayer();
		allPlayers = response.getPlayers();
		table = response.getTable();

		log.debug(String.format("Staring game with %d players", allPlayers.size()));
	}
}
