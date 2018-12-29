package com.tp.holdem.logic.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class PlayerNumber {
	private Integer number;

	public static PlayerNumber empty() {
		return new PlayerNumber(null);
	}

	public boolean exists() {
		return number != null;
	}
}
