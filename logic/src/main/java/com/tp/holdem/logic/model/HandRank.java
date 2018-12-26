package com.tp.holdem.logic.model;

import com.tp.holdem.model.common.Hands;
import io.vavr.collection.List;
import lombok.Value;

@Value
public class HandRank {
	private Hands hand;
	private List<Card> cardsThatMakeHand;

	public static HandRank from(Hands hand, List<Card> cards) {
		return new HandRank(hand, cards);
	}

	public static HandRank empty(Hands hand) {
		return new HandRank(hand, List.empty());
	}
}
