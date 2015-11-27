package com.tp.holdem;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

	@Override
	public int compare(Card card1, Card card2) {
		if(card1.getValue()<card2.getValue()){
			return -1;
		}
		else if(card1.getValue()>card2.getValue()){
			return 1;
		}
		else return 0;
	}
}