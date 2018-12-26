package com.tp.holdem.model.game;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HandRank {
	private Hands hand;
	private List<Card> cardsThatMakeHand;

	public static HandRank from(Hands hand, List<Card> cards) {
		return new HandRank(hand, cards);
	}

	public static HandRank empty(Hands hand) {
		return new HandRank(hand, Lists.newArrayList());
	}
}
