package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class Player {

  	private int number;
  	private List<Card> hand = new ArrayList<Card>();
  	private String name;
  	private boolean hisTurn = false;
  	private boolean hasDealerButton = false;
  	private boolean hasBigBlind = false;
  	private boolean hasSmallBlind = false;
  	private int betAmount = 0;
  	private int chipsAmount = 0;
  	private boolean isFolded = false;
  	
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

	public boolean isHisTurn() {
		return hisTurn;
	}

	public void setHisTurn(boolean hisTurn) {
		this.hisTurn = hisTurn;
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

	
}

