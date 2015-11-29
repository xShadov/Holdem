package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class Player {

  	private int number;
  	private List<Card> hand = new ArrayList<Card>();
  	private String name;
  	private boolean hasDealerButton = false;
  	private boolean hasBigBlind = false;
  	private boolean hasSmallBlind = false;
  	private int betAmount = 0;
  	private int chipsAmount = 0;
  	private boolean isFolded = false;
  	private int connectionId;
  	private boolean inGame = true;
  	private boolean isAllIn = false;
  	
  	public Player(){
  		
  	}
  	
	public Player(final int number){
    		this.number=number;
    		this.name="Player"+number;
  	}
  	
  	public Player(final int number, final String name){
    		this.number=number;
    		this.name=name;
  	}
  	
  	public void addCard(final Card card){
  		  hand.add(card);
  	}
  
  	public List<Card> getHand() {
  		  return hand;
  	}
  
  	public String getName() {
  		  return name;
  	}
  
  	public void setName(final String name) {
  		  this.name = name;
  	}
  
  	public int getNumber() {
  		  return number;
  	}	
  	
  	public boolean isFolded() {
		return isFolded;
	}

	public void setFolded(boolean isFolded) {
		this.isFolded = isFolded;
	}

	public boolean isHasDealerButton() {
		return hasDealerButton;
	}

	public void setHasDealerButton(boolean hasDealerButton) {
		this.hasDealerButton = hasDealerButton;
	}

	public boolean isHasBigBlind() {
		return hasBigBlind;
	}

	public void setHasBigBlind(boolean hasBigBlind) {
		this.hasBigBlind = hasBigBlind;
	}

	public boolean isHasSmallBlind() {
		return hasSmallBlind;
	}

	public void setHasSmallBlind(boolean hasSmallBlind) {
		this.hasSmallBlind = hasSmallBlind;
	}

	public int getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}

	public int getChipsAmount() {
		return chipsAmount;
	}

	public void setChipsAmount(int chipsAmount) {
		this.chipsAmount = chipsAmount;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isAllIn() {
		return isAllIn;
	}

	public void setAllIn(boolean isAllIn) {
		this.isAllIn = isAllIn;
	}

	
}

