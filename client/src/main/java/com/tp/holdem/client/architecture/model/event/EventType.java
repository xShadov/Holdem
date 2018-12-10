package com.tp.holdem.client.architecture.model.event;

import com.tp.holdem.client.architecture.model.common.GameStartMessage;
import com.tp.holdem.client.architecture.model.common.PlayerConnectMessage;

public enum EventType {
	PLAYER_CONNECTION(PlayerConnectMessage.class),
	GAME_START(GameStartMessage.class);

	private Class<? extends ServerResponse> clazz;

	EventType(Class<? extends ServerResponse> clazz) {
		this.clazz = clazz;
	}

	public Class<? extends ServerResponse> clazz() {
		return clazz;
	}
}
