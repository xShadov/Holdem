package com.tp.holdem.model.game;

import io.vavr.collection.List;

public enum Honour {
	TWO(2, "2"),
	THREE(3, "3"),
	FOUR(4, "4"),
	FIVE(5, "5"),
	SIX(6, "6"),
	SEVEN(7, "7"),
	EIGHT(8, "8"),
	NINE(9, "9"),
	TEN(10, "10"),
	JACK(11, "J"),
	QUEEN(12, "Q"),
	KING(13, "K"),
	ACE(14, "A");

	private int value;
	private String code;

	Honour(int value, String code) {
		this.value = value;
		this.code = code;
	}

	public String code() {
		return code;
	}

	public int value() {
		return value;
	}

	public boolean isRightAfter(Honour honour) {
		if (honour == ACE)
			return this == TWO;
		return this.ordinal() - 1 == honour.ordinal();
	}

	public static Honour coded(String code) {
		return List.of(values())
				.find(honour -> honour.code.equalsIgnoreCase(code))
				.getOrElseThrow(() -> new IllegalArgumentException(String.format("Invalid honour code %s", code)));
	}
}
