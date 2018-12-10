package com.tp.holdem.client.game;

import com.google.common.base.Preconditions;
import com.tp.holdem.client.architecture.bus.Action;
import com.tp.holdem.client.architecture.bus.GameObservable;
import com.tp.holdem.client.architecture.model.action.ActionType;
import com.tp.holdem.client.architecture.model.common.GameStartMessage;
import com.tp.holdem.client.architecture.model.common.PlayerConnectMessage;
import com.tp.holdem.client.model.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor
public class GameState implements GameObservable {
	private Player currentPlayer;
	private List<Player> allPlayers;

	@Override
	public void accept(Action action) {
		log.debug(String.format("Received action: %s", action.getActionType()));

		if (action.getActionType() == ActionType.PLAYER_CONNECT) {
			handlePlayerConnection(action);
		}

		if (action.getActionType() == ActionType.GAME_START) {
			handleGameStart(action);
		}
	}

	private void handlePlayerConnection(Action action) {
		log.debug("Handling player connection event");

		Preconditions.checkArgument(currentPlayer == null, "Player already connected");

		final PlayerConnectMessage response = action.instance(PlayerConnectMessage.class);

		currentPlayer = response.getPlayer();

		log.debug(String.format("Registered current player with number: %d", currentPlayer.getNumber()));
	}

	private void handleGameStart(Action action) {
		log.debug("Handling game start event");

		Preconditions.checkArgument(allPlayers == null, "Game is already started");

		final GameStartMessage response = action.instance(GameStartMessage.class);

		allPlayers = response.getPlayers();

		log.debug(String.format("Staring game with %d players", allPlayers.size()));
	}
}
