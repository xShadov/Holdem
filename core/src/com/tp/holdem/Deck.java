package com.tp.holdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

  	final private List<Card> cards = new ArrayList<Card>(52);
  	final private transient String[] suits = {"Spade", "Heart", "Diamond", "Club"};
  	final private transient String[] honours = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
  	
  	public Deck(){
    		for(int i=0; i<=12; i++)
    		{
      			cards.add(new Card(honours[i], suits[0]));
      			cards.add(new Card(honours[i], suits[1]));
      			cards.add(new Card(honours[i], suits[2]));
      			cards.add(new Card(honours[i], suits[3]));
    		}
  	}
  	
  	public void dealCards(final int numberOfCards, final List<Player> players){
    		shuffleCards();
    		for(int b=1; b<=numberOfCards; b++)
    		{
      			for(final Player player : players)
      			{
      					if(!player.isFolded() && player.isInGame()){
	        				player.addCard(cards.get(0));
	        				cards.remove(0);
      					}
      			}
    		}
  	}
  
  	public int howManyCardsLeft(){
  		  return cards.size();
  	}
  	
  	public boolean isEmpty(){
  		  if(cards.size()==0) return true;
  		  return false;
  	}
  	
  	public List<Card> getCards() {
  		  return cards;
  	}
      
  	public void shuffleCards(){
  		  Collections.shuffle(cards);
  	}
  	
  	public Card drawCard()
  	{
  		Card card = cards.get(0);
  		cards.remove(0);
  		return card;
  	}
	
}
