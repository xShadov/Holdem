package com.tp.holdem.client.architecture.action;

import io.vavr.control.Option;

public enum ActionType {
	CHECK,
	FOLD,
	ALLIN,
	CALL,
	RAISE(PlayerRaiseAction.class),
	BET(PlayerBetAction.class);

	private Class<? extends ActionRequest> clazz;

	ActionType() {

	}

	ActionType(Class<? extends ActionRequest> clazz) {
		this.clazz = clazz;
	}

	public Option<Class<? extends ActionRequest>> clazz() {
		return Option.of(clazz);
	}
}
