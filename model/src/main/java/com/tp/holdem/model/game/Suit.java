package com.tp.holdem.model.game;

import io.vavr.collection.List;

public enum Suit {
	SPADE('S'), HEART('H'), DIAMOND('D'), CLUB('C');

	private char code;

	Suit(char code) {
		this.code = code;
	}

	public char code() {
		return code;
	}

	public static Suit coded(char code) {
		return List.of(values())
				.find(suit -> suit.code == Character.toUpperCase(code))
				.getOrElseThrow(() -> new IllegalArgumentException(String.format("Invalid suit code %s", code)));
	}
}
