package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private int pot;
	private List<Card> cardsOnTable;
	
	
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
}
