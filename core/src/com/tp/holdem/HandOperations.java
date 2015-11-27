package com.tp.holdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandOperations {

	public static List<Card> cardsThatMakeDeck;
	
	public static HandRank findHandRank(List<Card> hand, List<Card> tableCards){
		List<Card> cards = new ArrayList<Card>();
		cards.addAll(hand);
		cards.addAll(tableCards);
		
		if (isARoyalFlush(cards)) {
	        return new HandRank(HandRankingEnum.ROYAL_FLUSH, cardsThatMakeDeck);
	    } else if (isAStraightFlush(cards)) {
	        return new HandRank(HandRankingEnum.STRAIGHT_FLUSH, cardsThatMakeDeck);
	    } else if (isAFourOfAKind(cards)) {
	        return new HandRank(HandRankingEnum.FOUR_OF_A_KIND, cardsThatMakeDeck);
	    } else if (isAFullHouse(cards)) {
	        return new HandRank(HandRankingEnum.FULL_HOUSE, cardsThatMakeDeck);
	    } else if (isAFlush(cards)) {
	        return new HandRank(HandRankingEnum.FLUSH, cardsThatMakeDeck);
	    } else if (isAStraight(cards)) {
	        return new HandRank(HandRankingEnum.STRAIGHT, cardsThatMakeDeck);
	    } else if (isThreeOfAKind(cards)) {
	        return new HandRank(HandRankingEnum.THREE_OF_A_KIND, cardsThatMakeDeck);
	    } else if (isTwoPair(cards)) {
	        return new HandRank(HandRankingEnum.TWO_PAIR, cardsThatMakeDeck);
	    } else if (isPair(cards)) {
	        return new HandRank(HandRankingEnum.PAIR, cardsThatMakeDeck);
	    } else {
	        return new HandRank(HandRankingEnum.HIGH_CARD, cardsThatMakeDeck);
	    }
	}
	
	private static boolean isAFullHouse(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
		boolean isFullHouse = false;
		boolean isTwo = false;
		boolean isThree = false;
		for(int i=0; i<cards.size()-1;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour()){
				isTwo = true;
				i=i+1;
			}
			else if(cards.get(i).getHonour()==cards.get(i+1).getHonour() 
					&& cards.get(i).getHonour()==cards.get(i+2).getHonour()){
				isThree = true;
				i=i+2;
			}
			else{
				cardsThatMakeDeck.remove(i);
			}
		}
		return isFullHouse;
	}

	private static boolean isTwoPair(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
		int pairsFound = 0;
		for(int i=0; i<cards.size()-1;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour()){
				pairsFound++;
				i=i+1;
			}
			else{
				if(i<=1 && pairsFound==0) cardsThatMakeDeck.remove(i);
				else if(pairsFound==1 && i<4) cardsThatMakeDeck.remove(i);
			}
		}
		return pairsFound==2;
	}

	private static boolean isPair(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
		boolean isPair = false;
		for(int i=0; i<cards.size()-1;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour()){
				isPair = true;
				i=i+1;
			}
			else{
				if(i<=1 && !isPair) cardsThatMakeDeck.remove(i);
				else if(isPair && i<4) cardsThatMakeDeck.remove(i);
			}
		}
		return isPair;
	}

	private static boolean isThreeOfAKind(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
		boolean isThreeOfKind = false;
		for(int i=0; i<cards.size()-2;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour() 
					&& cards.get(i).getHonour()==cards.get(i+2).getHonour()){
				isThreeOfKind = true;
				i=i+2;
			}
			else{
				if(i<=1 && !isThreeOfKind) cardsThatMakeDeck.remove(i);
				else if(isThreeOfKind && i<5) cardsThatMakeDeck.remove(i);
			}
		}
		return isThreeOfKind;
	}

	private static boolean isAFourOfAKind(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
		boolean isFourOfKind = false;
		for(int i=0; i<cards.size()-3;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour() 
					&& cards.get(i).getHonour()==cards.get(i+2).getHonour()
					&& cards.get(i).getHonour()==cards.get(i+3).getHonour()){
				isFourOfKind = true;
				i=i+3;
			}
			else{
				if(i<=1 && !isFourOfKind) cardsThatMakeDeck.remove(i);
				else if(isFourOfKind && i<6) cardsThatMakeDeck.remove(i);
			}
		}
		return isFourOfKind;
	}

	private static boolean isAStraightFlush(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
		if(isAStraight(cards)){
			if(isAFlush(cardsThatMakeDeck)){
				return true;
			}
		}
		return false;
	}

	private static boolean isARoyalFlush(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
		if(isAStraight(cards)){
			if(isAFlush(cardsThatMakeDeck)){
				if(cardsThatMakeDeck.get(4).getHonour()=="Ace"){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isAStraight(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
	    int cardsInARow = 1;
	    int mistakes = 0;
	    boolean isStraight = false;
	    for(int i = 0; i<cards.size()-1; i++){
	    	if(cards.get(i).getValue()+1==cards.get(i+1).getValue()){
	    		cardsInARow++;
	    		if(cardsInARow==5) isStraight = true;
	    	}
	    	else if(cards.get(i).getHonour()=="Ace" && cards.get(i+1).getHonour()=="2"){
	    		cardsInARow++;
	    		if(cardsInARow==5) isStraight = true;
	    	}
	    	else if(cards.get(i).getValue()==cards.get(i+1).getValue()){
	    		mistakes++;
	    		cardsThatMakeDeck.remove(i);
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
	    
	    if(cardsInARow==6 || cardsInARow==7) trimDeckDown(cardsInARow-6);
	    return isStraight;
	}

	public static boolean isAFlush(List<Card> cards) {
		Collections.sort(cards, new CardComparator());
		cardsThatMakeDeck = cards;
	    int noOfClubs = 0;
	    int noOfSpades = 0;
	    int noOfHearts = 0;
	    int noOfDiamonds = 0;
	    boolean isFlush = false;
	    for(Card card : cards){
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
	    		if(!cardsThatMakeDeck.get(i).getSuit().equals("Club")) cardsThatMakeDeck.remove(i);
	    	}
	    	if(noOfClubs>5) trimDeckDown(noOfClubs-6);
	    }
	    else if(noOfHearts>=5){
	    	isFlush = true;
	    	for(int i=0; i<cardsThatMakeDeck.size();i++){
	    		if(!cardsThatMakeDeck.get(i).getSuit().equals("Heart")) cardsThatMakeDeck.remove(i);
	    	}
	    	if(noOfHearts>5) trimDeckDown(noOfHearts-6);
	    }
	    else if(noOfDiamonds>=5){
	    	isFlush = true;
	    	for(int i=0; i<cardsThatMakeDeck.size();i++){
	    		if(!cardsThatMakeDeck.get(i).getSuit().equals("Diamond")) cardsThatMakeDeck.remove(i);
	    	}
	    	if(noOfDiamonds>5) trimDeckDown(noOfDiamonds-6);
	    }
	    else if(noOfSpades>=5){
	    	isFlush = true;
	    	for(int i=0; i<cardsThatMakeDeck.size();i++){
	    		if(!cardsThatMakeDeck.get(i).getSuit().equals("Spade")) cardsThatMakeDeck.remove(i);
	    	}
	    	if(noOfSpades>5) trimDeckDown(noOfSpades-6);
	    }
	    return isFlush;
	}

	
	private static void trimDeckUp(int i) {
		for(int a=i+1; a<7; a++){
			cardsThatMakeDeck.remove(a);
		}
	}
	
	private static void trimDeckDown(int i) {
		for(int a=i; a>=0;a--){
			cardsThatMakeDeck.remove(a);
		}
	}


}
