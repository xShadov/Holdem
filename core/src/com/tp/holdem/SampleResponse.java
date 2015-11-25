package com.tp.holdem;

import java.util.List;

public class SampleResponse {

	public String text="";
	public String TAG="";
	public List<Player> players;
	public int number = 0;
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<Player> getPlayers() {
		return players;
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

	public SampleResponse(){
		
	}
	
	public SampleResponse(String TAG, String text){
		this.text=text;
	}
	
	public SampleResponse(String TAG, List<Player> players){
		this.players = players;
		this.TAG = TAG;
	}
	
	public SampleResponse(String TAG, int number){
		this.TAG = TAG;
		this.number = number;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}
}

