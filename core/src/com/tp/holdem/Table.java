package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private int pot;
	private List<Card> cardsOnTable;
	private int smallBlindAmount;
	private int bigBlindAmount;
	
	public Table()
	{
		this.pot = 0;
		this.cardsOnTable = new ArrayList<Card>();
	}
	
	public void setPot(int newPot)
	{
		this.pot=newPot;
	}
	
	public int getPot()
	{
		return pot;
	}
	
	public List<Card> getCardList()
	{
		return cardsOnTable;
	}
	
	public void addCard(final Card card)
	{
		cardsOnTable.add(card);
	}

	public List<Card> getCardsOnTable() {
		return cardsOnTable;
	}

	public void setCardsOnTable(List<Card> cardsOnTable) {
		this.cardsOnTable = cardsOnTable;
	}

	public int getSmallBlindAmount() {
		return smallBlindAmount;
	}

	public void setSmallBlindAmount(int smallBlindAmount) {
		this.smallBlindAmount = smallBlindAmount;
	}

	public int getBigBlindAmount() {
		return bigBlindAmount;
	}

	public void setBigBlindAmount(int bigBlindAmount) {
		this.bigBlindAmount = bigBlindAmount;
	}
}
