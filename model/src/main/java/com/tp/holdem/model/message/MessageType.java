package com.tp.holdem.model.message;


public enum MessageType {
	PLAYER_CONNECTION(PlayerConnectMessage.class),
	UPDATE_STATE(UpdateStateMessage.class),
	PLAYER_ACTION(PlayerActionMessage.class);

	private Class<? extends ServerMessage> clazz;

	MessageType(Class<? extends ServerMessage> clazz) {
		this.clazz = clazz;
	}

	public Class<? extends ServerMessage> clazz() {
		return clazz;
	}
}
