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
		cards.add(new Card("5", "Heart"));
		cards.add(new Card("Queen", "Diamond"));
		cards.add(new Card("4", "Spade"));
		cards.add(new Card("8", "Spade"));
		Collections.sort(cards, new CardComparator());
		//for(Card card : cards){
	//		System.out.println(card.getHonour());
	//	}
    } 
	
	@Test                                        
    public final void testHand() {          
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("Jack", "Spade"));
		List<Card> tableCards = new ArrayList<Card>();
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		System.out.println(HandOperations.findHandRank(0, cards, tableCards));
    } 
	
	@Test
	public final void compareSingleHand(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("5", "Spade"));
		List<Card> tableCards = new ArrayList<Card>();
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("7", "Spade"));
		List<HandRank> hands = new ArrayList<HandRank>();
		hands.add(HandOperations.findHandRank(0, cards, tableCards));
		cards = new ArrayList<Card>();
		cards.add(new Card("Queen", "Heart"));
		cards.add(new Card("King", "Heart"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("Ace", "Heart"));
		tableCards.add(new Card("10", "Heart"));
		tableCards.add(new Card("Jack", "Heart"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("7", "Spade"));
		hands.add(HandOperations.findHandRank(1, cards, tableCards));
		HandRankComparator comp = new HandRankComparator();
		if(comp.compare(hands.get(1), hands.get(1))==0){
			System.out.println(1);
		}
		else System.out.println(0);
	}
	@Test                                        
    public final void handCompare() {          
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("Jack", "Spade"));
		List<Card> tableCards = new ArrayList<Card>();
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		List<HandRank> hands = new ArrayList<HandRank>();
		
		hands.add(HandOperations.findHandRank(0, cards, tableCards));
		cards = new ArrayList<Card>();
		cards.add(new Card("10", "Heart"));
		cards.add(new Card("Jack", "Heart"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", "Heart"));
		tableCards.add(new Card("4", "Heart"));
		tableCards.add(new Card("Ace", "Heart"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		hands.add(HandOperations.findHandRank(1, cards, tableCards));

		cards = new ArrayList<Card>();
		cards.add(new Card("10", "Heart"));
		cards.add(new Card("Jack", "Heart"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", "Heart"));
		tableCards.add(new Card("4", "Heart"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("9", "Spade"));
		hands.add(HandOperations.findHandRank(2, cards, tableCards));
		
		cards = new ArrayList<Card>();
		cards.add(new Card("10", "Heart"));
		cards.add(new Card("Jack", "Heart"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", "Heart"));
		tableCards.add(new Card("4", "Heart"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("9", "Spade"));
		hands.add(HandOperations.findHandRank(3, cards, tableCards));
		
		Collections.sort(hands, new HandRankComparator());
		for(HandRank hand : hands){
			System.out.println(hand);
		}
    } 

}
