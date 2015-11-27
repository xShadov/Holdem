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
		Collections.sort(cards, new CardComparator());
		
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
	
	public static boolean isAFullHouse(List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
		boolean isTwo = false;
		boolean isThree = false;
		for(int i=0; i<cards.size()-1;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour()){
				isTwo = true;
				i=i+1;
				if(i<5){
					if(cards.get(i).getHonour()==cards.get(i+1).getHonour()){
						isTwo = false;
						isThree = true;
						i=i+1;
					}
				}
			}
			else{
				cardsThatMakeDeck.set(i, null);
			}
		}
	    cleanDeck();
		return isTwo&&isThree;
	}

	private static boolean isTwoPair(List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
		int pairsFound = 0;
		for(int i=0; i<cards.size()-1;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour()){
				pairsFound++;
				i=i+1;
			}
			else{
				if(i<=1 && pairsFound==0) cardsThatMakeDeck.set(i, null);
				else if(pairsFound==1 && i<4) cardsThatMakeDeck.set(i, null);
			}
		}
	    cleanDeck();
		return pairsFound==2;
	}

	private static boolean isPair(List<Card> cards) {
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

	private static boolean isThreeOfAKind(List<Card> cards) {
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

	private static boolean isAFourOfAKind(List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
		boolean isFourOfKind = false;
		for(int i=0; i<cards.size()-3;i++){
			if(cards.get(i).getHonour()==cards.get(i+1).getHonour() 
					&& cards.get(i).getHonour()==cards.get(i+2).getHonour()
					&& cards.get(i).getHonour()==cards.get(i+3).getHonour()){
				isFourOfKind = true;
				i=i+3;
			}
			else{
				if(i<=1 && !isFourOfKind) cardsThatMakeDeck.set(i, null);
				else if(isFourOfKind && i<6) cardsThatMakeDeck.set(i, null);
			}
		}
	    cleanDeck();
		return isFourOfKind;
	}

	private static boolean isAStraightFlush(List<Card> cards) {
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

	private static boolean isARoyalFlush(List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
		boolean isRoyalFlush = false;
		if(isAStraight(cards)){
			if(isAFlush(cardsThatMakeDeck)){
				if(cardsThatMakeDeck.get(4).getHonour()=="Ace"){
					isRoyalFlush = true;
				}
			}
		}
	    cleanDeck();
		return isRoyalFlush;
	}

	public static boolean isAStraight(List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
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
	    		cardsThatMakeDeck.set(i, null);
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
	    return isStraight;
	}

	public static boolean isAFlush(List<Card> cards) {
		cardsThatMakeDeck = new ArrayList<Card>(cards);
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

	
	private static void trimDeckUp(int i) {
		for(int a=i+1; a<cardsThatMakeDeck.size(); a++){
			if(cardsThatMakeDeck.get(a)!=null){
				cardsThatMakeDeck.remove(a);
				a--;
			}
		}
	}
	
	private static void trimDeckDown(int i) {
		for(int a=i; a>=0;a--){
			if(cardsThatMakeDeck.get(a)!=null){
				cardsThatMakeDeck.remove(a);
			}
		}
	}
	
	private static void cleanDeck(){
		for(int i=0; i<cardsThatMakeDeck.size(); i++){
			Card card = cardsThatMakeDeck.get(i);
			if(card==null){
				cardsThatMakeDeck.remove(card);
				i--;
			}
		}
	}


}
