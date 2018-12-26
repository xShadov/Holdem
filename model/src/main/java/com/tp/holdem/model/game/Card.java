package com.tp.holdem.model.game;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Card {
	private Suit suit;
	private Honour honour;
	private int value;

	private Card(final Suit suit, final Honour honour) {
		this.suit = suit;
		this.honour = honour;
		this.value = this.honour.value();
	}

	public static Card from(Suit suit, Honour honour) {
		return new Card(suit, honour);
	}

	public static Card coded(String code) {
		return Card.from(Suit.coded(code.charAt(code.length() - 1)), Honour.coded(code.substring(0, code.length() - 1)));
	}

	public String code() {
		return String.format("%s%s", honour.code(), suit.code());
	}
}