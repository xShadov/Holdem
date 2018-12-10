package com.tp.holdem.client.compare;

import com.tp.holdem.client.model.Card;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

	@Override
	public int compare(final Card card1, final Card card2) {
		if (card1.getValue() < card2.getValue()) {
			return -1;
		} else if (card1.getValue() > card2.getValue()) {
			return 1;
		} else
			return 0;
	}
}