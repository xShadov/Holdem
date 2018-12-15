package com.tp.holdem.client.game;

import com.tp.holdem.client.architecture.action.Action;
import com.tp.holdem.client.architecture.action.GameObservable;
import com.tp.holdem.client.architecture.message.MessageBus;
import com.tp.holdem.client.architecture.message.ServerObservable;
import com.tp.holdem.model.message.Message;
import com.tp.holdem.model.message.MessageType;
import com.tp.holdem.model.message.PlayerActionMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameWatcher implements ServerObservable, GameObservable {
	private final MessageBus toState;
	private final MessageBus toElements;
	private final MessageBus toClient;

	public GameWatcher(MessageBus toState, MessageBus toElements, MessageBus toClient) {
		this.toState = toState;
		this.toElements = toElements;
		this.toClient = toClient;
	}

	@Override
	public void accept(Message message) {
		log.debug(String.format("Accepting message: %s", message.getMessageType()));

		if (message.getMessageType() == MessageType.PLAYER_CONNECTION) {
			handlePlayerConnection(message);
		}

		if (message.getMessageType() == MessageType.UPDATE_STATE) {
			handleUpdateState(message);
		}
	}

	@Override
	public void accept(Action action) {
		log.debug(String.format("Sending player action to client: %s", action.getMove()));

		toClient.message(Message.from(MessageType.PLAYER_ACTION, PlayerActionMessage.from(action.getMove(), action.getBetAmount())));
	}

	private void handlePlayerConnection(Message message) {
		log.debug("Sending player connection message to state");

		toState.message(message);
	}

	private void handleUpdateState(Message message) {
		log.debug("Sending update state message to state and elements");

		toState.message(message);
		toElements.message(message);
	}
}
