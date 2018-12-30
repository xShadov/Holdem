package com.tp.holdem.common.message.dto;

import com.tp.holdem.common.model.Honour;
import com.tp.holdem.common.model.Suit;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardDTO {
	private Suit suit;
	private Honour honour;
	private int value;

	@Builder
	private CardDTO(final Suit suit, final Honour honour) {
		this.suit = suit;
		this.honour = honour;
		this.value = this.honour.value();
	}
	public static CardDTO from(Suit suit, Honour honour) {
		return new CardDTO(suit, honour);
	}

	public String code() {
		return String.format("%s%s", honour.code(), suit.code());
	}
}