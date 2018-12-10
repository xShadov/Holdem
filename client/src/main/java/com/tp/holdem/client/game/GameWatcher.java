package com.tp.holdem.client.game;

import com.tp.holdem.client.architecture.bus.*;
import com.tp.holdem.client.architecture.model.action.ActionType;
import com.tp.holdem.client.architecture.model.common.GameStartMessage;
import com.tp.holdem.client.architecture.model.common.PlayerConnectMessage;
import com.tp.holdem.client.architecture.model.event.EventType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameWatcher implements ServerObservable, GameObservable {
	private final ActionBus toState;

	public GameWatcher(ActionBus toState) {
		this.toState = toState;
	}

	@Override
	public void accept(Event event) {
		log.debug(String.format("Accepting event: %s", event.getEventType()));

		if (event.getEventType() == EventType.PLAYER_CONNECTION) {
			handlePlayerConnection(event);
		}

		if (event.getEventType() == EventType.GAME_START) {
			handleGameStart(event);
		}
	}

	@Override
	public void accept(Action action) {

	}

	private void handlePlayerConnection(Event event) {
		log.debug("Sending player connection event to state");

		final PlayerConnectMessage response = event.instance(PlayerConnectMessage.class);

		final Action connectAction = Action.from(ActionType.PLAYER_CONNECT, response);
		toState.message(connectAction);
	}

	private void handleGameStart(Event event) {
		log.debug("Sending game start event to state");

		final GameStartMessage response = event.instance(GameStartMessage.class);

		final Action action = Action.from(ActionType.GAME_START, response);
		toState.message(action);
	}
}
