package com.tp.holdem;

import java.util.List;

public class HandRank {

	private HandRankingEnum hand;
	private List<Card> cardsThatMakeDeck;
	
	public HandRank(){
		
	}
	
	public HandRank(HandRankingEnum hand, List<Card> cardsThatMakeDeck){
		this.hand=hand;
		this.cardsThatMakeDeck=cardsThatMakeDeck;
	}

	@Override
	public String toString(){
		String string = hand.toString();
		for(Card card : cardsThatMakeDeck){
			string+=" "+card.getHonour()+" "+card.getSuit();
		}
		return string;
	}
	
	public HandRankingEnum getHand() {
		return hand;
	}

	public void setHand(HandRankingEnum hand) {
		this.hand = hand;
	}

	public List<Card> getCardsThatMakeDeck() {
		return cardsThatMakeDeck;
	}

	public void setCardsThatMakeDeck(List<Card> cardsThatMakeDeck) {
		this.cardsThatMakeDeck = cardsThatMakeDeck;
	}
}
