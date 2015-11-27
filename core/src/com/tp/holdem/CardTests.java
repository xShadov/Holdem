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
		cards.add(new Card("Jack", "Heart"));
		cards.add(new Card("Jack", "Diamond"));
		cards.add(new Card("Queen", "Spade"));
		cards.add(new Card("Queen", "Heart"));
		cards.add(new Card("4", "Spade"));
		cards.add(new Card("8", "Spade"));
		Collections.sort(cards, new CardComparator());

    } 
	
	@Test                                        
    public final void testHand() {          
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("2", "Spade"));
		cards.add(new Card("3", "Spade"));
		List<Card> tableCards = new ArrayList<Card>();
		tableCards.add(new Card("4", "Spade"));
		tableCards.add(new Card("5", "Spade"));
		tableCards.add(new Card("6", "Spade"));
		tableCards.add(new Card("Ace", "Diamond"));
		tableCards.add(new Card("8", "Spade"));
		System.out.println(HandOperations.findHandRank(cards, tableCards));
    } 

}
