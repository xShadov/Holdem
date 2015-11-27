package com.tp.holdem;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CardTests {

	@Test                                        
    public final void testSetCardSuit() {          
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("Jack", "Spade"));
		cards.add(new Card("Queen", "Spade"));
		cards.add(new Card("Ace", "Spade"));
		cards.add(new Card("4", "Spade"));
		cards.add(new Card("8", "Spade"));
		Collections.sort(cards, new CardComparator());
		for(Card card : cards){
			System.out.println(card.getHonour()+" "+card.getValue());
		}
    } 

}
