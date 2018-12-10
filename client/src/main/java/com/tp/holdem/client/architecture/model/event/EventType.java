package com.tp.holdem.client.architecture.model.event;

import com.tp.holdem.client.architecture.model.common.PlayerConnectMessage;
import com.tp.holdem.client.architecture.model.common.UpdateStateMessage;

public enum EventType {
	PLAYER_CONNECTION(PlayerConnectMessage.class),
	UPDATE_STATE(UpdateStateMessage.class);

	private Class<? extends ServerResponse> clazz;

	EventType(Class<? extends ServerResponse> clazz) {
		this.clazz = clazz;
	}

	public Class<? extends ServerResponse> clazz() {
		return clazz;
	}
}
