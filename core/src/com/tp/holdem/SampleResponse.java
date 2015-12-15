package com.tp.holdem;

import java.util.List;

public class SampleResponse {

	public String text="";
	public String TAG="";
	public List<Player> players;
	public List<Card> cards;
	public List<List<Card>> cards2;
	public PokerTable pokerTable;
	public int number = 0;
	public int maxBetOnTable = 0;
	public List<String> possibleOptions;

	public SampleResponse(){
		
	}
	
	public SampleResponse(String TAG){
		this.TAG = TAG;
	}
	
	public SampleResponse(String TAG, String text){
		this.TAG = TAG;
		this.text=text;
	}
	
	public SampleResponse(String TAG, PokerTable pokerTable){
		this.TAG = TAG;
		this.pokerTable = pokerTable;
	}
	
	public SampleResponse(String TAG, List<Player> players){
		this.players = players;
		this.TAG = TAG;
	}

	public SampleResponse(String TAG, List<Card> cards, boolean dummy){
		this.cards = cards;
		this.TAG = TAG;
	}
	
	public SampleResponse(String TAG, List<List<Card>> cards2, boolean dummy, boolean dummy2){
		this.cards2 = cards2;
		this.TAG = TAG;
	}
	
	public SampleResponse(String TAG, int number){
		this.TAG = TAG;
		this.number = number;
	}
	
	public SampleResponse(String TAG, int number, int maxBetOnTable, List<String> possibleOptions){
		this.TAG = TAG;
		this.number = number;
		this.maxBetOnTable = maxBetOnTable;
		this.possibleOptions = possibleOptions;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public List<Card> getCards()
	{
		return cards;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public String getTAG() {
		return TAG;
	}

	public void setTAG(String tAG) {
		TAG = tAG;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}

	public PokerTable getTable() {
		return pokerTable;
	}

	public void setTable(PokerTable pokerTable) {
		this.pokerTable = pokerTable;
	}

	public int getMaxBetOnTable() {
		return maxBetOnTable;
	}

	public void setMaxBetOnTable(int maxBetOnTable) {
		this.maxBetOnTable = maxBetOnTable;
	}

	public List<String> getPossibleOptions() {
		return possibleOptions;
	}

	public void setPossibleOptions(List<String> possibleOptions) {
		this.possibleOptions = possibleOptions;
	}

	public List<List<Card>> getCards2() {
		return cards2;
	}

	public void setCards2(List<List<Card>> cards2) {
		this.cards2 = cards2;
	}
}

