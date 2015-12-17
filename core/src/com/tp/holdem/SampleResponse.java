package com.tp.holdem;

import java.util.List;

public class SampleResponse {

	private String text="";
	private String tag="";
	private List<Player> players;
	private List<Card> cards;
	private List<List<Card>> cards2;
	private PokerTable pokerTable;
	private int number = 0;
	private boolean revealed = false;
	private int maxBetOnTable = 0;
	private List<String> possibleOptions;

	public SampleResponse(){
		
	}
	
	public SampleResponse(final String TAG){
		this.tag = TAG;
	}
	
	public SampleResponse(final String TAG, final String text){
		this.tag = TAG;
		this.text=text;
	}
	
	public SampleResponse(final String TAG, final PokerTable pokerTable){
		this.tag = TAG;
		this.pokerTable = pokerTable;
	}
	
	public SampleResponse(final String TAG, final List<Player> players){
		this.players = players;
		this.tag = TAG;
	}

	public SampleResponse(final String TAG, final List<Card> cards, final boolean dummy){
		this.cards = cards;
		this.tag = TAG;
	}
	
	public SampleResponse(final String TAG, final List<List<Card>> cards2, final boolean dummy, final boolean dummy2){
		this.cards2 = cards2;
		this.tag = TAG;
		this.revealed = dummy;
	}
	
	public SampleResponse(final String TAG, final int number){
		this.tag = TAG;
		this.number = number;
	}
	
	public SampleResponse(final String TAG, final int number, final int maxBetOnTable, final List<String> possibleOptions){
		this.tag = TAG;
		this.number = number;
		this.maxBetOnTable = maxBetOnTable;
		this.possibleOptions = possibleOptions;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public List<Card> getCards()
	{
		return cards;
	}
	public void setPlayers(final List<Player> players) {
		this.players = players;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(final String tAG) {
		tag = tAG;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(final String text){
		this.text = text;
	}

	public PokerTable getTable() {
		return pokerTable;
	}

	public void setTable(final PokerTable pokerTable) {
		this.pokerTable = pokerTable;
	}

	public int getMaxBetOnTable() {
		return maxBetOnTable;
	}

	public void setMaxBetOnTable(final int maxBetOnTable) {
		this.maxBetOnTable = maxBetOnTable;
	}

	public List<String> getPossibleOptions() {
		return possibleOptions;
	}

	public void setPossibleOptions(final List<String> possibleOptions) {
		this.possibleOptions = possibleOptions;
	}

	public List<List<Card>> getCards2() {
		return cards2;
	}

	public void setCards2(final List<List<Card>> cards2) {
		this.cards2 = cards2;
	}

	public boolean isRevealed() {
		return revealed;
	}

	public void setRevealed(boolean revealed) {
		this.revealed = revealed;
	}
}

