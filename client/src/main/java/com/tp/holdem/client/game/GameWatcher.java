package com.tp.holdem.client.game;

import com.tp.holdem.client.architecture.bus.*;
import com.tp.holdem.client.architecture.model.action.ActionType;
import com.tp.holdem.client.architecture.model.common.UpdateStateMessage;
import com.tp.holdem.client.architecture.model.common.PlayerConnectMessage;
import com.tp.holdem.client.architecture.model.event.EventType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameWatcher implements ServerObservable, GameObservable {
	private final ActionBus toState;
	private final ActionBus toElements;

	public GameWatcher(ActionBus toState, ActionBus toElements) {
		this.toState = toState;
		this.toElements = toElements;
	}

	@Override
	public void accept(Event event) {
		log.debug(String.format("Accepting event: %s", event.getEventType()));

		if (event.getEventType() == EventType.PLAYER_CONNECTION) {
			handlePlayerConnection(event);
		}

		if (event.getEventType() == EventType.UPDATE_STATE) {
			handleUpdateState(event);
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

	private void handleUpdateState(Event event) {
		log.debug("Sending update state event to state and elements");

		final UpdateStateMessage response = event.instance(UpdateStateMessage.class);

		final Action action = Action.from(ActionType.UPDATE_STATE, response);
		toState.message(action);
		toElements.message(action);
	}
}
