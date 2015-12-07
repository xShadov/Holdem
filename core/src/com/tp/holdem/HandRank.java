package com.tp.holdem;

import java.util.List;

public class HandRank {

	private HandRankingEnum hand;
	private List<Card> cardsThatMakeDeck;
	private int playerNumber;
	
	public HandRank(){
		
	}
	
	public HandRank(int playerNumber, HandRankingEnum hand, List<Card> cardsThatMakeDeck){
		this.hand=hand;
		this.playerNumber = playerNumber;
		this.cardsThatMakeDeck=cardsThatMakeDeck;
	}
	
	public HandRankingEnum getHand() {
		return hand;
	}
	
	@Override
	public String toString(){
		String string = playerNumber+" ";
		string+= hand.toString();
		for(Card card : cardsThatMakeDeck){
			string+=" "+card.getHonour()+" "+card.getSuit();
		}
		return string;
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

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
}
