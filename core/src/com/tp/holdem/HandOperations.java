package com.tp.holdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandOperations {

	public static List<Card> cardsThatMakeDeck;
	
	public static HandRank findHandRank(final int playerNumber, final List<Card> hand, final List<Card> tableCards){
		final List<Card> cards = new ArrayList<Card>();
		cards.addAll(hand);
		cards.addAll(tableCards);
		Collections.sort(cards, new CardComparator());
		
		if (isARoyalFlush(cards)) {
			return new HandRank(playerNumber, HandRankingEnum.ROYAL_FLUSH, cardsThatMakeDeck);
	    } else if (isAStraightFlush(cards)) {
	    	return new HandRank(playerNumber, HandRankingEnum.STRAIGHT_FLUSH, cardsThatMakeDeck);
	    } else if (isAFourOfAKind(cards)) {
	        return new HandRank(playerNumber, HandRankingEnum.FOUR_OF_A_KIND, cardsThatMakeDeck);
	    } else if (isAFullHouse(cards)) {
	        return new HandRank(playerNumber, HandRankingEnum.FULL_HOUSE, cardsThatMakeDeck);
	    } else if (isAFlush(cards)) {
	        return new HandRank(playerNumber, HandRankingEnum.FLUSH, cardsThatMakeDeck);
	    } else if (isAStraight(cards)) {
	        return new HandRank(playerNumber, HandRankingEnum.STRAIGHT, cardsThatMakeDeck);
	    } else if (isThreeOfAKind(cards)) {
	        return new HandRank(playerNumber, HandRankingEnum.THREE_OF_A_KIND, cardsThatMakeDeck);
	    } else if (isTwoPair(cards)) {
	        return new HandRank(playerNumber, HandRankingEnum.TWO_PAIR, cardsThatMakeDeck);
	    } else if (isPair(cards)) {
	        return new HandRank(playerNumber, HandRankingEnum.PAIR, cardsThatMakeDeck);
	    } else {
	        return new HandRank(playerNumber, HandRankingEnum.HIGH_CARD, getFiveMaxCards(cards));
	    }
	}
	
	public static boolean isAFullHouse(final List<Card> cards) {
		final List<Card> three = new ArrayList<Card>();
		final List<Card> two = new ArrayList<Card>();
		for(int i=0; i<cards.size()-2; i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour()
					&& cards.get(i).getHonour()==cards.get(i+2).getHonour()){
				if(three.size()==0){
					three.add(cards.get(i));
					three.add(cards.get(i+1));
					three.add(cards.get(i+2));
				} else {
					three.removeAll(three);
					three.add(cards.get(i));
					three.add(cards.get(i+1));
					three.add(cards.get(i+2));
				}
			}
		}
		final List<Card> cardsLeft = new ArrayList<Card>(cards);
		cardsLeft.removeAll(three);
		for(int i=0; i<cardsLeft.size()-1;i++){
			if(cardsLeft.get(i).getHonour()==cardsLeft.get(i+1).getHonour()){
				if(two.size()==0){
					two.add(cardsLeft.get(i));
					two.add(cardsLeft.get(i+1));
				} else {
					two.removeAll(two);
					two.add(cardsLeft.get(i));
					two.add(cardsLeft.get(i+1));
				}
			}
		}
		cardsThatMakeDeck = new ArrayList<Card>(two);
		cardsThatMakeDeck.addAll(three);
	    cleanDeck();
	    Collections.sort(cardsThatMakeDeck, new CardComparator());
		return cardsThatMakeDeck.size()==5;
	}

	private static boolean isTwoPair(final List<Card> cards) {
		final List<Card> helperList = new ArrayList<Card>();
		final List<Card> cardsLeft = new ArrayList<Card>(cards);
		int pairsFound = 0;
		for(int i=0; i<cards.size()-1;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour()){
				if(pairsFound<=1){
					pairsFound++;
					helperList.add(cards.get(i));
					helperList.add(cards.get(i+1));
					i++;
				}
				else{
					helperList.remove(0);
					helperList.remove(0);
					helperList.add(cards.get(i));
					helperList.add(cards.get(i+1));
					i++;
					pairsFound++;
				}
			}
		}
		if(pairsFound>=2){
			cardsLeft.removeAll(helperList);
			helperList.add(findMaxCard(cardsLeft));
			cardsThatMakeDeck = new ArrayList<Card>(helperList);
		    cleanDeck();
		    Collections.sort(cardsThatMakeDeck, new CardComparator());
		}
		return pairsFound>=2;
	}

	private static boolean isPair(final List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
		boolean isPair = false;
		for(int i=0; i<cards.size()-1;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour()){
				isPair = true;
				i=i+1;
			}
			else{
				if(i<=1 && !isPair) cardsThatMakeDeck.set(i, null);
				else if(isPair && i<4) cardsThatMakeDeck.set(i, null);
			}
		}
	    cleanDeck();
		return isPair;
	}

	private static boolean isThreeOfAKind(final List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
		boolean isThreeOfKind = false;
		for(int i=0; i<cards.size()-2;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour() 
					&& cards.get(i).getHonour()==cards.get(i+2).getHonour()){
				isThreeOfKind = true;
				i=i+2;
			}
			else{
				if(i<=1 && !isThreeOfKind) cardsThatMakeDeck.set(i, null);
				else if(isThreeOfKind && i<5) cardsThatMakeDeck.set(i, null);
			}
		}
	    cleanDeck();
		return isThreeOfKind;
	}

	private static boolean isAFourOfAKind(final List<Card> cards) {
		final List<Card> cardsLeft = new ArrayList<Card>(cards);
		final List<Card> helperList = new ArrayList<Card>();
		boolean isFourOfKind = false;
		for(int i=0; i<cards.size()-3;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour() 
					&& cards.get(i).getHonour()==cards.get(i+2).getHonour()
					&& cards.get(i).getHonour()==cards.get(i+3).getHonour()){
				isFourOfKind = true;
				helperList.add(cards.get(i));
				helperList.add(cards.get(i+1));
				helperList.add(cards.get(i+2));
				helperList.add(cards.get(i+3));
				i=i+3;
			}
		}
		if(isFourOfKind){
			cardsLeft.removeAll(helperList);
			helperList.add(findMaxCard(cardsLeft));
			cardsThatMakeDeck = new ArrayList<Card>(helperList);
		    cleanDeck();
		    Collections.sort(cardsThatMakeDeck, new CardComparator());
		}
		return isFourOfKind;
	}

	private static boolean isAStraightFlush(final List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
		boolean isStraightFlush = false;
		if(isAStraight(cards)){
			if(isAFlush(cardsThatMakeDeck)){
				isStraightFlush = true;
			}
		}
	    cleanDeck();
		return isStraightFlush;
	}

	private static boolean isARoyalFlush(final List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
		boolean isRoyalFlush = false;
		if(isAStraight(cards)){
			if(isAFlush(cardsThatMakeDeck)){
				if(cardsThatMakeDeck.get(4).getHonour().equals("Ace")){
					isRoyalFlush = true;
				}
			}
		}
	    cleanDeck();
		return isRoyalFlush;
	}

	public static boolean isAStraight(final List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
	    int cardsInARow = 1;
	    int mistakes = 0;
	    boolean isStraight = false;
	    
	    if(cards.get(0).getHonour().equals("2") && cards.get(cards.size()-1).getHonour().equals("Ace")){
	    	cardsInARow = 2;
	    }
	    
	    for(int i = 0; i<cards.size()-1; i++){
	    	if(cards.get(i).getValue()+1==cards.get(i+1).getValue()){
	    		cardsInARow++;
	    		if(cardsInARow==5) isStraight = true;
	    	}
	    	else if(cards.get(i).getValue()==cards.get(i+1).getValue()){
	    		mistakes++;
	    	}
	    	else{
	    		mistakes++;
	    		if(mistakes>2) return false;
	    		if(!isStraight){
	    			cardsInARow = 1;
	    			trimDeckDown(i);
	    		}
	    		else if(isStraight){
	    			trimDeckUp(i);
	    			break;
	    		}
	    	}
	    } 

	    cleanDeck();
	    if(cardsInARow==6 || cardsInARow==7) trimDeckDown(cardsInARow-6);
	    cleanDeck();
	    return isStraight;
	}

	public static boolean isAFlush(final List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
	    int noOfClubs = 0;
	    int noOfSpades = 0;
	    int noOfHearts = 0;
	    int noOfDiamonds = 0;
	    boolean isFlush = false;
	    for(final Card card : cards){
	    	if(card.getSuit().equals("Spade")){
	    		noOfSpades++;
	    	}
	    	else if(card.getSuit().equals("Heart")){
	    		noOfHearts++;
	    	}
	    	else if(card.getSuit().equals("Diamond")){
	    		noOfDiamonds++;
	    	}
	    	else if(card.getSuit().equals("Club")){
	    		noOfClubs++;
	    	}
	    }
	    
	    if(noOfClubs>=5){
	    	isFlush = true;
	    	for(int i=0; i<cardsThatMakeDeck.size();i++){
	    		if(!cardsThatMakeDeck.get(i).getSuit().equals("Club")) cardsThatMakeDeck.set(i, null);
	    	}
	    	if(noOfClubs>5) trimDeckDown(noOfClubs-6);
	    }
	    else if(noOfHearts>=5){
	    	isFlush = true;
	    	for(int i=0; i<cardsThatMakeDeck.size();i++){
	    		if(!cardsThatMakeDeck.get(i).getSuit().equals("Heart")) cardsThatMakeDeck.set(i, null);
	    	}
	    	if(noOfHearts>5) trimDeckDown(noOfHearts-6);
	    }
	    else if(noOfDiamonds>=5){
	    	isFlush = true;
	    	for(int i=0; i<cardsThatMakeDeck.size();i++){
	    		if(!cardsThatMakeDeck.get(i).getSuit().equals("Diamond")) cardsThatMakeDeck.set(i, null);
	    	}
	    	if(noOfDiamonds>5) trimDeckDown(noOfDiamonds-6);
	    }
	    else if(noOfSpades>=5){
	    	isFlush = true;
	    	for(int i=0; i<cardsThatMakeDeck.size();i++){
	    		if(!cardsThatMakeDeck.get(i).getSuit().equals("Spade")) cardsThatMakeDeck.set(i, null);
	    	}
	    	if(noOfSpades>5) trimDeckDown(noOfSpades-6);
	    }
	    cleanDeck();
	    return isFlush;
	}

	
	private static void trimDeckUp(final int i) {
		for(int a=i+1; a<cardsThatMakeDeck.size(); a++){
			cardsThatMakeDeck.set(a, null);
		}
	}
	
	private static void trimDeckDown(final int i) {
		for(int a=i; a>=0; a--){
			cardsThatMakeDeck.set(a, null);
		}
	}
	
	private static void cleanDeck(){
		for(int i=0; i<cardsThatMakeDeck.size(); i++){
			final Card card = cardsThatMakeDeck.get(i);
			if(card==null){
				cardsThatMakeDeck.remove(card);
				i--;
			}
		}
	}
	
	private static Card findMaxCard(final List<Card> cardsLeft) {
		Collections.sort(cardsLeft, new CardComparator());
		return cardsLeft.get((cardsLeft.size()-1));
	}

	private static List<Card> getFiveMaxCards(List<Card> cards){
		final List<Card> fiveTop = new ArrayList<Card>(cards);
		fiveTop.remove(0);
		fiveTop.remove(0);
		return fiveTop;
	}
}
