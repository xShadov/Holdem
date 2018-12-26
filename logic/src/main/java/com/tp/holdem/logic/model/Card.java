package com.tp.holdem.logic.model;

import com.tp.holdem.model.common.Honour;
import com.tp.holdem.model.common.Suit;
import com.tp.holdem.model.message.dto.CardDTO;
import lombok.Builder;
import lombok.Value;

@Value
public class Card {
	private Suit suit;
	private Honour honour;
	private int value;

	@Builder
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

	public CardDTO toDTO() {
		return CardDTO.builder()
				.honour(honour)
				.suit(suit)
				.build();
	}
}