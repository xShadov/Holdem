package com.tp.holdem;

import java.util.List;

public class HandRank {

	private HandRankingEnum hand;
	private Card finalCard;
	private List<Card> cardsThatMakeDeck;
	
	public HandRank(){
		
	}
	
	public HandRank(HandRankingEnum hand, List<Card> cardsThatMakeDeck){
		this.hand=hand;
		this.cardsThatMakeDeck=cardsThatMakeDeck;
		findFinalCard();
	}

	private void findFinalCard() {
		
	}

	public HandRankingEnum getHand() {
		return hand;
	}

	public void setHand(HandRankingEnum hand) {
		this.hand = hand;
	}

	public Card getFinalCard() {
		return finalCard;
	}

	public void setFinalCard(Card finalCard) {
		this.finalCard = finalCard;
	}

	public List<Card> getCardsThatMakeDeck() {
		return cardsThatMakeDeck;
	}

	public void setCardsThatMakeDeck(List<Card> cardsThatMakeDeck) {
		this.cardsThatMakeDeck = cardsThatMakeDeck;
	}
}
